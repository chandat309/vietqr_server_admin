package com.vietqradminbe.web.controllers;

import com.vietqradminbe.application.services.ActionLogService;
import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.ActionLog;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services.TransactionService;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtUtil;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.request.RequestFilterTransactionRequest;
import com.vietqradminbe.web.dto.response.APIResponse;
import com.vietqradminbe.web.dto.response.TransactionReceivePaginationResponseDTO;
import com.vietqradminbe.web.dto.response.interfaces.TransactionReceiveAdminListDTO;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    static Logger logger = Logger.getLogger(TransactionController.class);

    TransactionService transactionService;
    JwtUtil jwtUtil;
    UserService userService;
    ActionLogService actionLogService;


    @GetMapping("/transactions")
    public APIResponse<TransactionReceivePaginationResponseDTO> getTransactions(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        APIResponse<TransactionReceivePaginationResponseDTO> response = new APIResponse<>();
        try {
            HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            // Extract Bearer token from the Authorization header
            String authorizationHeader = currentRequest.getHeader("Authorization");
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);  // Remove "Bearer " prefix

                //lay username tu token va lay user tu username
                String username = jwtUtil.extractUsernameFromToken(token.replace("Bearer ", ""));
                User user = userService.getUserByUsername(username);

                ActionLog actionLog = new ActionLog();
                actionLog.setUsername(username);
                actionLog.setId(UUID.randomUUID().toString());
                actionLog.setEmail(user.getEmail());
                actionLog.setFirstname(user.getFirstname());
                actionLog.setLastname(user.getLastname());
                actionLog.setPhoneNumber(user.getPhoneNumber());
                actionLog.setCreateAt(TimeHelperUtil.getCurrentTime());
                actionLog.setUpdateAt("");
                actionLog.setUser(user);
                actionLog.setDescription("User :" + user.getUsername() + " " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getPhoneNumber() + " have just get all trans at " + TimeHelperUtil.getCurrentTime());
                actionLogService.createActionLog(actionLog);
            }

            TransactionReceivePaginationResponseDTO trans = transactionService.getTransactionsWithPaginationAllFilter(page, size);
            logger.info(TransactionController.class + ": INFO: trans: " + trans.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(trans);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            logger.error(TransactionController.class + ": ERROR: trans: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1005");
        }
        return response;
    }


    @GetMapping("/transactions/v2")
    public APIResponse<TransactionReceivePaginationResponseDTO> getTransactionsV2(
            @RequestParam(value = "from", required = false) Long from,
            @RequestParam(value = "to", required = false) Long to,
            @RequestParam(value = "bankAccount", required = false) String bankAccount,
            @RequestParam(value = "referenceNumber", required = false) String referenceNumber,
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "terminalCode", required = false) String terminalCode,
            @RequestParam(value = "subCode", required = false) String subCode,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "transStatus", required = false) Integer transStatus,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ) {
        APIResponse<TransactionReceivePaginationResponseDTO> response = new APIResponse<>();
        try {
            HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            // Extract Bearer token from the Authorization header
            String authorizationHeader = currentRequest.getHeader("Authorization");
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);  // Remove "Bearer " prefix

                //lay username tu token va lay user tu username
                String username = jwtUtil.extractUsernameFromToken(token.replace("Bearer ", ""));
                User user = userService.getUserByUsername(username);

                ActionLog actionLog = new ActionLog();
                actionLog.setUsername(username);
                actionLog.setId(UUID.randomUUID().toString());
                actionLog.setEmail(user.getEmail());
                actionLog.setFirstname(user.getFirstname());
                actionLog.setLastname(user.getLastname());
                actionLog.setPhoneNumber(user.getPhoneNumber());
                actionLog.setCreateAt(TimeHelperUtil.getCurrentTime());
                actionLog.setUpdateAt("");
                actionLog.setUser(user);
                actionLog.setDescription("User :" + user.getUsername() + " " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getPhoneNumber() + " have just get all users at " + TimeHelperUtil.getCurrentTime());
                actionLogService.createActionLog(actionLog);
            }

            TransactionReceivePaginationResponseDTO transResponse = transactionService.getTransactionsWithPagination(from, to, bankAccount,
                    referenceNumber, orderId, terminalCode, subCode, transStatus, page, size);
            logger.info(TransactionController.class + ": INFO: trans: " + transResponse.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(transResponse);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            logger.error(TransactionController.class + ": ERROR: trans: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1005");
        }
        return response;
    }


    @PostMapping("/transactions/v3")
    public APIResponse<TransactionReceivePaginationResponseDTO> getTransactionsV3(
            @RequestBody RequestFilterTransactionRequest filterTransactionRequest
    ) {
        APIResponse<TransactionReceivePaginationResponseDTO> response = new APIResponse<>();
        try {
            HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            // Extract Bearer token from the Authorization header
            String authorizationHeader = currentRequest.getHeader("Authorization");
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);  // Remove "Bearer " prefix

                //lay username tu token va lay user tu username
                String username = jwtUtil.extractUsernameFromToken(token.replace("Bearer ", ""));
                User user = userService.getUserByUsername(username);

                ActionLog actionLog = new ActionLog();
                actionLog.setUsername(username);
                actionLog.setId(UUID.randomUUID().toString());
                actionLog.setEmail(user.getEmail());
                actionLog.setFirstname(user.getFirstname());
                actionLog.setLastname(user.getLastname());
                actionLog.setPhoneNumber(user.getPhoneNumber());
                actionLog.setCreateAt(TimeHelperUtil.getCurrentTime());
                actionLog.setUpdateAt("");
                actionLog.setUser(user);
                actionLog.setDescription("User :" + user.getUsername() + " " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getPhoneNumber() + " have just get all users at " + TimeHelperUtil.getCurrentTime());
                actionLogService.createActionLog(actionLog);
            }

            TransactionReceivePaginationResponseDTO transResponse = transactionService.getTransactionsWithPaginationByOption(filterTransactionRequest);
            logger.info(TransactionController.class + ": INFO: trans: " + transResponse.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(transResponse);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            logger.error(TransactionController.class + ": ERROR: trans: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(400);
            response.setMessage("E1005");
        }
        return response;
    }
}
