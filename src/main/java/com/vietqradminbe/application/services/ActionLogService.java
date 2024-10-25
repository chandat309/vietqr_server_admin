package com.vietqradminbe.application.services;

import com.vietqradminbe.application.services.interfaces.IActionLogService;
import com.vietqradminbe.domain.models.ActionLog;
import com.vietqradminbe.domain.repositories.ActionLogRepository;
import com.vietqradminbe.web.dto.response.PagingDTO;
import com.vietqradminbe.web.dto.response.interfaces.ActionLogListDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ActionLogService implements IActionLogService {

    @Autowired
    ActionLogRepository actionLogRepository;


    @Override
    public void createActionLog(ActionLog actionLog) {
        actionLogRepository.save(actionLog);
    }

    @Override
    public PagingDTO<ActionLogListDTO> getAllActionLogs(int page, int size) {
        int offset = (page - 1) * size;
        List<ActionLogListDTO> logs = actionLogRepository.getAllLogs(offset, size);

        int total = actionLogRepository.getTotalAllLogs();

        boolean hasNext = (offset + logs.size()) < total;

        PagingDTO<ActionLogListDTO> pagingDTO = new PagingDTO<>();
        pagingDTO.setItems(logs);
        pagingDTO.setPage(page);
        pagingDTO.setLimit(size);
        pagingDTO.setTotal(total);
        pagingDTO.setHasNext(hasNext);
        return pagingDTO;
    }


}
