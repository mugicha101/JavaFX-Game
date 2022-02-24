module com.example.demo {
  requires javafx.controls;
  requires javafx.fxml;


  opens application to javafx.fxml;
  exports application;
  exports application.bulletAttr;
  opens application.bulletAttr to javafx.fxml;
  exports application.bullet;
  opens application.bullet to javafx.fxml;
}