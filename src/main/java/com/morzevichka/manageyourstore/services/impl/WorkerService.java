package com.morzevichka.manageyourstore.services.impl;

import com.morzevichka.manageyourstore.dto.WorkerSalesDto;
import com.morzevichka.manageyourstore.entity.Role;
import com.morzevichka.manageyourstore.entity.Worker;

import javax.naming.AuthenticationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface WorkerService {
    Worker findWorker(Long id);

    void saveWorker(Worker worker);

    void deleteWorker(Worker worker);

    void updateWorker(Worker worker);

    List<Worker> findAllWorkers();

    Worker findWorkerByUsername(String name);

    Worker loginWorker(String username, String password) throws AuthenticationException;

    void registerWorker(Worker worker);

    void changePassword(String username, String currentPassword, String newPassword);

    List<WorkerSalesDto> workerSalesBetween(LocalDate var1, LocalDate var2);

}
