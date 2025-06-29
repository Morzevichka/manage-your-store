package com.morzevichka.manageyourstore.services;

import com.morzevichka.manageyourstore.dto.UserSesssion;
import com.morzevichka.manageyourstore.dto.WorkerSalesDto;
import com.morzevichka.manageyourstore.entity.Role;
import com.morzevichka.manageyourstore.entity.Worker;
import com.morzevichka.manageyourstore.repository.WorkerRepository;
import com.morzevichka.manageyourstore.services.impl.WorkerService;
import com.morzevichka.manageyourstore.utils.PasswordUtils;
import com.morzevichka.manageyourstore.utils.ValidationUtil;
import org.hibernate.HibernateException;

import javax.naming.AuthenticationException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository = new WorkerRepository();

    public Worker findWorker(Long id) {
        return workerRepository.findById(id).orElse(null);
    }

    public void saveWorker(Worker worker) {
        workerRepository.save(worker);
    }

    public void deleteWorker(Worker worker) {
        workerRepository.delete(worker);
    }

    public void updateWorker(Worker worker) {
        workerRepository.update(worker);
    }

    public List<Worker> findAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker findWorkerByUsername(String name) {
        return workerRepository.findByUsername(name).orElse(null);
    }

    public Worker loginWorker(String username, String password) throws AuthenticationException {
        Worker worker = workerRepository.findByUsername(username)
                .filter(currentWorker -> PasswordUtils.checkPassword(password, currentWorker.getPasswordHash()))
                .orElseThrow(() -> new AuthenticationException("Непральный логин или пароль"));

        worker.setLastLogin(Timestamp.valueOf(LocalDateTime.now()));
        updateWorker(worker);
        UserSesssion.getInstance().setWorker(worker);
        return worker;
    }

    public void registerWorker(String firstName, String secondName, String username, String password, Role role, BigDecimal salary) {
        try {
            ValidationUtil.isValidPassword(password);
        } catch (Exception e) {
            throw e;
        }

        Worker worker = new Worker();
        worker.setFirstName(firstName);
        worker.setSecondName(secondName);
        worker.setUsername(username);
        worker.setPasswordHash(PasswordUtils.encrypt(password));
        worker.setRole(role);
        worker.setSalary(salary);
        worker.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        try {
            saveWorker(worker);
        } catch (HibernateException e) {
            throw new HibernateException("Произошла ошибка. Попробуйте еще раз!");
        }
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        if (!PasswordUtils.checkPassword(currentPassword, UserSesssion.getInstance().getWorker().getPasswordHash())) {
            throw new IllegalStateException("Неверный текущий пароль");
        }
        if (currentPassword.equals(newPassword)) {
            throw new IllegalStateException("Новый пароль не должен совпадать со старым");
        }
        try {
            ValidationUtil.isValidPassword(newPassword);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        Worker worker = findWorkerByUsername(username);
        worker.setPasswordHash(PasswordUtils.encrypt(newPassword));
        updateWorker(worker);
    }


    public List<WorkerSalesDto> workerSalesBetween(LocalDate var1, LocalDate var2) {
        Timestamp from = var1 != null ? Timestamp.valueOf(LocalDateTime.of(var1, LocalTime.MIDNIGHT)) : Timestamp.from(Instant.EPOCH);
        Timestamp to = var2 != null ? Timestamp.valueOf(LocalDateTime.of(var2, LocalTime.MIDNIGHT)) : Timestamp.valueOf(LocalDateTime.now());

        return workerRepository.findWorkerSalesBetween(from, to);
    }
}
