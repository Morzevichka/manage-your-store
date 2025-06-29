module com.morzevichka.manageyourstore {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires io.github.cdimascio.dotenv.java;
    requires com.oracle.database.jdbc;
    requires jBCrypt;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires javafx.swing;
    requires cache.api;
    requires java.management;
    requires jakarta.transaction;


    exports com.morzevichka.manageyourstore;
    opens com.morzevichka.manageyourstore to javafx.fxml;
    exports com.morzevichka.manageyourstore.entity;
    opens com.morzevichka.manageyourstore.entity to javafx.fxml, org.hibernate.orm.core;
    exports com.morzevichka.manageyourstore.dto;
    opens com.morzevichka.manageyourstore.dto to javafx.fxml;
    exports com.morzevichka.manageyourstore.repository;
    opens com.morzevichka.manageyourstore.repository to org.hibernate.orm.core;
    exports com.morzevichka.manageyourstore.services;
    opens com.morzevichka.manageyourstore.services to javafx.fxml, org.hibernate.orm.core;
    exports com.morzevichka.manageyourstore.controllers.auth;
    opens com.morzevichka.manageyourstore.controllers.auth to javafx.fxml;
    exports com.morzevichka.manageyourstore.controllers.account;
    opens com.morzevichka.manageyourstore.controllers.account to javafx.fxml;
    exports com.morzevichka.manageyourstore.controllers.main;
    opens com.morzevichka.manageyourstore.controllers.main to javafx.fxml;
    exports com.morzevichka.manageyourstore.controllers.center.categories;
    opens com.morzevichka.manageyourstore.controllers.center.categories to javafx.fxml;
    exports com.morzevichka.manageyourstore.controllers.center.products;
    opens com.morzevichka.manageyourstore.controllers.center.products to javafx.fxml;
    exports com.morzevichka.manageyourstore.controllers.center.purchase;
    opens com.morzevichka.manageyourstore.controllers.center.purchase to javafx.fxml;
    exports com.morzevichka.manageyourstore.controllers.center.report;
    opens com.morzevichka.manageyourstore.controllers.center.report to javafx.fxml;
}