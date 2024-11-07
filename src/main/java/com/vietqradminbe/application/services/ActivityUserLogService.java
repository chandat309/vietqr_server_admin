package com.vietqradminbe.application.services;

import com.vietqradminbe.application.services.interfaces.IActivityUserLogService;
import com.vietqradminbe.domain.models.ActivityUserLog;
import com.vietqradminbe.domain.repositories.ActivityUserLogRepository;
import com.vietqradminbe.web.dto.response.PagingDTO;
import com.vietqradminbe.web.dto.response.interfaces.ActivityUserLogListDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ActivityUserLogService implements IActivityUserLogService {

    @Autowired
    ActivityUserLogRepository activityUserLogRepository;


    @Override
    public void createActivityUserLog(ActivityUserLog actionLog) {
        activityUserLogRepository.save(actionLog);
    }

    @Override
    public PagingDTO<ActivityUserLogListDTO> getAllActivityUserLogs(int page, int size) {
        int offset = (page - 1) * size;
        List<ActivityUserLogListDTO> logs = activityUserLogRepository.getAllLogs(offset, size);

        int total = activityUserLogRepository.getTotalAllLogs();

        boolean hasNext = (offset + logs.size()) < total;

        PagingDTO<ActivityUserLogListDTO> pagingDTO = new PagingDTO<>();
        pagingDTO.setItems(logs);
        pagingDTO.setPage(page);
        pagingDTO.setLimit(size);
        pagingDTO.setTotal(total);
        pagingDTO.setHasNext(hasNext);
        return pagingDTO;
    }


}
