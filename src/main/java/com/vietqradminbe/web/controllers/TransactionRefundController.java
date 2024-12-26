package com.vietqradminbe.web.controllers;

import com.example.grpc.GetLogRequest;
import com.example.grpc.GetTransactionRefundRequest;
import com.vietqradminbe.application.services.ActivityUserLogService;
import com.vietqradminbe.application.services.UserService;
import com.vietqradminbe.application.services.grpc.TransactionRefundServiceClient;
import com.vietqradminbe.application.services.grpc.response.TransactionRefundGrpcDTOImpl;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.ActivityUserLog;
import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.infrastructure.adapters.database.mysql.transaction.services.TransactionRefundService;
import com.vietqradminbe.infrastructure.configuration.security.utils.JwtUtil;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import com.vietqradminbe.web.dto.response.APIResponse;
import com.vietqradminbe.web.dto.response.interfaces.TransactionRefundAdminDetailDTO;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionRefundController {

    static Logger logger = Logger.getLogger(TransactionRefundController.class);

    TransactionRefundService transactionRefundService;
    JwtUtil jwtUtil;
    UserService userService;
    ActivityUserLogService activityUserLogService;
    TransactionRefundServiceClient transactionRefundServiceClient;

    @GetMapping("/transactions-refund")
    public ResponseEntity<APIResponse<List<TransactionRefundAdminDetailDTO>>> getTransactionRefundAdminDetail(
            @RequestParam(value = "referenceNumber") String referenceNumber
    ) {
        APIResponse<List<TransactionRefundAdminDetailDTO>> response = new APIResponse<>();
        try {
            HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            // Extract Bearer token from the Authorization header
            String authorizationHeader = currentRequest.getHeader("Authorization");
            String token = null;
            List<TransactionRefundAdminDetailDTO> trans = transactionRefundService.getTransactionRefundAdminDetail(referenceNumber);


            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);  // Remove "Bearer " prefix

                //lay username tu token va lay user tu username
                String username = jwtUtil.extractUsernameFromToken(token.replace("Bearer ", ""));
                User user = userService.getUserByUsername(username);

                ActivityUserLog activityUserLog = new ActivityUserLog();
                activityUserLog.setUsername(username);
                activityUserLog.setId(UUID.randomUUID().toString());
                activityUserLog.setEmail(user.getEmail());
                activityUserLog.setFirstname(user.getFirstname());
                activityUserLog.setLastname(user.getLastname());
                activityUserLog.setPhoneNumber(user.getPhoneNumber());
                activityUserLog.setTimeLog(TimeHelperUtil.getCurrentTime());
                activityUserLog.setUser(user);
                activityUserLog.setActionJson(trans.toString());
                activityUserLog.setGroupFunctionId("71db4741-8f18-4f7c-ba5b-cdbeaab8b45e");
                activityUserLog.setFunctionId("eafb7d72-0d11-47da-a9cb-6fd0797f8029");
                activityUserLog.setDescription("User :" + user.getUsername() + " " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getPhoneNumber() + " have just get detail transaction refund " + TimeHelperUtil.getCurrentTime());
                activityUserLogService.createActivityUserLog(activityUserLog);
            }

            logger.info(TransactionController.class + ": INFO: trans: " + trans.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(trans);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            logger.error(TransactionController.class + ": ERROR: trans: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/transactions-refund/demo-grpc")
    public ResponseEntity<APIResponse<List<TransactionRefundGrpcDTOImpl>>> getTransactionRefundAdminDetailGrpc(
            @RequestParam(value = "referenceNumber") String referenceNumber
    ) {
        APIResponse<List<TransactionRefundGrpcDTOImpl>> response = new APIResponse<>();
        try {
            HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();

            // Extract Bearer token from the Authorization header
            String authorizationHeader = currentRequest.getHeader("Authorization");
            String token = null;
            GetTransactionRefundRequest grpcRequest = GetTransactionRefundRequest.newBuilder()
                    .setReferenceNumber(referenceNumber.trim().replace("\n", ""))
                    .build();
            List<TransactionRefundGrpcDTOImpl> trans = transactionRefundServiceClient.getTransactionRefundGrpc(grpcRequest);


            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);  // Remove "Bearer " prefix

                //lay username tu token va lay user tu username
                String username = jwtUtil.extractUsernameFromToken(token.replace("Bearer ", ""));
                User user = userService.getUserByUsername(username);

                ActivityUserLog activityUserLog = new ActivityUserLog();
                activityUserLog.setUsername(username);
                activityUserLog.setId(UUID.randomUUID().toString());
                activityUserLog.setEmail(user.getEmail());
                activityUserLog.setFirstname(user.getFirstname());
                activityUserLog.setLastname(user.getLastname());
                activityUserLog.setPhoneNumber(user.getPhoneNumber());
                activityUserLog.setTimeLog(TimeHelperUtil.getCurrentTime());
                activityUserLog.setUser(user);
                activityUserLog.setActionJson("trans.toString()");
                activityUserLog.setGroupFunctionId("71db4741-8f18-4f7c-ba5b-cdbeaab8b45e");
                activityUserLog.setFunctionId("eafb7d72-0d11-47da-a9cb-6fd0797f8029");
                activityUserLog.setDescription("User :" + user.getUsername() + " " + user.getEmail() + " " + user.getFirstname() + " " + user.getLastname() + " " + user.getPhoneNumber() + " have just get detail transaction refund " + TimeHelperUtil.getCurrentTime());
                activityUserLogService.createActivityUserLog(activityUserLog);
            }

            logger.info(TransactionController.class + ": INFO: trans: " + trans.toString()
                    + " at: " + System.currentTimeMillis());
            response.setCode(200);
            response.setMessage("Get successfully!");
            response.setResult(trans);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            logger.error(TransactionController.class + ": ERROR: trans: " + e.getMessage()
                    + " at: " + System.currentTimeMillis());
            response.setCode(500);
            response.setMessage("E1005");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
