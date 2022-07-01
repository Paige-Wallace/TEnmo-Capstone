package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {

    List<Transfer> getAllApprovedTransfers(long accountId);

    List<Transfer> getAllPendingTransfers(long accountId);

    Transfer getTransferById(long transferId);

    Transfer newTransfer(long accountFrom, long accountTo, BigDecimal amount);

    Transfer newRequest(long userFrom, long userTo, BigDecimal amount);

    boolean rejectRequest(long transferI);

    boolean acceptRequest(long userFrom, long userTo, BigDecimal amount, long transferI);

}