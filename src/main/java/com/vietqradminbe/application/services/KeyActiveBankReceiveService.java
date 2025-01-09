package com.vietqradminbe.application.services;

import com.example.grpc.SaveKeyActiveRequest;
import com.example.grpc.SaveKeyActiveResponse;
import com.vietqradminbe.application.services.grpc.KeyActiveGrpcClient;
import com.vietqradminbe.application.services.interfaces.IKeyActiveBankReceiveService;
import com.vietqradminbe.domain.models.KeyActiveBankReceive;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.domain.repositories.KeyActiveBankReceiveRepository;
import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.entities.KeyActiveBankReceiveEntity;
import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.repositories.KeyActiveBankReceiveTransactionRepository;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.infrastructure.configuration.utils.BcryptKeyUtil;
import com.vietqradminbe.infrastructure.configuration.utils.EnvironmentUtil;
import com.vietqradminbe.web.dto.request.GenerateKeyBankDTO;
import com.vietqradminbe.web.dto.response.interfaces.KeyActiveBankReceiveDTO;
import com.vietqradminbe.web.dto.response.PagingDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyActiveBankReceiveService implements IKeyActiveBankReceiveService {

    KeyActiveBankReceiveRepository keyActiveBankReceiveRepository;
    KeyActiveGrpcClient keyActiveGrpcClient;


    @Override
    public List<String> checkDuplicatedKeyActives(List<String> keys) {
        return keyActiveBankReceiveRepository.checkDuplicatedKeyActives(keys);
    }

    @Override
    public List<String> generateAndSaveKeys(User user, GenerateKeyBankDTO dto) {
        List<String> keyActives = generateKeyActiveWithCheckDuplicated(dto.getNumOfKeys());
        List<KeyActiveBankReceive> entitiesAdmin = new ArrayList<>();
        List<KeyActiveBankReceiveEntity> entitiesTransaction = new ArrayList<>();

        for (String keyActive : keyActives) {
            String secretKey = generateSecretKey();
            String valueActive = generateValueActive(keyActive, secretKey, dto.getDuration());

            String vietnamTimeString = TimeHelperUtil.getCurrentTime();
            String expirationTimeString = TimeHelperUtil.calculateExpirationTime(vietnamTimeString, dto.getDuration());

            // Tạo đối tượng KeyActiveBankReceive cho admin database
            KeyActiveBankReceive entityAdmin = KeyActiveBankReceive.builder()
                    .id(UUID.randomUUID().toString())
                    .keyActive(keyActive)
                    .secretKey(secretKey)
                    .valueActive(valueActive)
                    .duration(dto.getDuration())
                    .createAt(vietnamTimeString)
                    .createBy(user.getUsername())
                    .bankAccountActivated("")
                    .activationTime("")
                    .expirationTime(expirationTimeString)
                    .status(0)
                    .user(user)
                    .build();
            entitiesAdmin.add(entityAdmin);

            // Chuyển đổi thời gian tạo sang epoch
            long createAtAsLong = TimeHelperUtil.convertVietnamTimeStringToEpochLong(vietnamTimeString);

            // Gọi gRPC client để lưu thông tin
            SaveKeyActiveRequest request = SaveKeyActiveRequest.newBuilder()
                    .setId(entityAdmin.getId())
                    .setKeyActive(keyActive)
                    .setSecretKey(secretKey)
                    .setValueActive(valueActive)
                    .setDuration(dto.getDuration())
                    .setCreateAt(createAtAsLong)
                    .setStatus(0)
                    .setVersion(0)
                    .setBankId("")
                    .build();

            SaveKeyActiveResponse response = keyActiveGrpcClient.saveKeyActive(request);

            if (!response.getSuccess()) {
                log.error("Failed to save keyActive via gRPC: {}", response.getMessage());
                throw new RuntimeException("gRPC call failed: " + response.getMessage());
            }
        }

        // Lưu vào admin database
        keyActiveBankReceiveRepository.saveAll(entitiesAdmin);
        return keyActives;
    }

    public PagingDTO<KeyActiveBankReceiveDTO> getKeyActiveBankReceives(String startDate, String endDate, int page, int size) {
        if (startDate == null || startDate.isEmpty()) {
            startDate = TimeHelperUtil.getEpochStartTime();
        }
        if (endDate == null   || endDate.isEmpty()) {
            endDate = TimeHelperUtil.getCurrentTime();
        }

        int offset = (page - 1) * size;
        List<KeyActiveBankReceiveDTO> keys = keyActiveBankReceiveRepository.findAllByCreateAtBetween(startDate, endDate, offset, size);
        int total = keyActiveBankReceiveRepository.countByCreateAtBetween(startDate, endDate);

        boolean hasNext = (offset + keys.size()) < total;

        return PagingDTO.<KeyActiveBankReceiveDTO>builder()
                .items(keys)
                .page(page)
                .limit(size)
                .total(total)
                .hasNext(hasNext)
                .build();
    }

    @Override
    public List<KeyActiveBankReceiveDTO> getKeyDetailsByKeys(List<String> keys) {
        return keyActiveBankReceiveRepository.findByKeyActiveIn(keys);
    }

    private String generateSecretKey() {
        return UUID.randomUUID().toString();
    }

    private String generateValueActive(String keyActive, String secretKey, int duration) {
        return BcryptKeyUtil.hashKeyActive(keyActive, secretKey, duration);
    }

    private List<String> generateKeyActiveWithCheckDuplicated(int numOfKeys) {
        List<String> keys = new ArrayList<>();
        keys = generateMultikeyActive(numOfKeys);
        // check duplicated;
        List<String> keysDuplicated = new ArrayList<>();
        do {
            keysDuplicated = checkDuplicatedKeyActives(keys);
            if (keysDuplicated.isEmpty()) {
                break;
            }
            // remove duplicated
            for (String key : keysDuplicated) {
                keys.remove(key);
            }
            // generate new key
            List<String> newKeys = generateMultikeyActive(keysDuplicated.size());
            keys.addAll(newKeys);
        } while (!keysDuplicated.isEmpty());
        return keys;
    }

    private List<String> generateMultikeyActive(int numOfKeys) {
        Set<String> keys = new HashSet<>();
        for (int i = 0; i < numOfKeys; i++) {
            String key = generateKeyActive();
            keys.add(key);
        }
        return new ArrayList<>(keys);
    }

    private String generateKeyActive() {
        Random random = new Random();
        int length = EnvironmentUtil.getLengthKeyActiveBank();
        String characters = EnvironmentUtil.getCharactersKeyActiveBank();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
