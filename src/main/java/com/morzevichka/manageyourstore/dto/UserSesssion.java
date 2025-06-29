package com.morzevichka.manageyourstore.dto;

import com.morzevichka.manageyourstore.entity.Worker;
import com.morzevichka.manageyourstore.services.WorkerServiceImpl;
import com.morzevichka.manageyourstore.services.impl.WorkerService;

public class UserSesssion {
    private static UserSesssion instance;
    private Worker worker;

    public static UserSesssion getInstance() {
        if (instance == null) {
            instance = new UserSesssion();
        }
        return instance;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void clearSession() {
        instance = null;
    }

    public void updateUser() {
        WorkerService workerServiceImpl = new WorkerServiceImpl();
        this.worker = workerServiceImpl.findWorker(getWorker().getId());
    }
}