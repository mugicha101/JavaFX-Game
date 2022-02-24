module com.example.demo {
  requires javafx.controls;
  requires javafx.fxml;


  opens application to javafx.fxml;
  exports application;
  exports application.bullet.bulletAttr;
  opens application.bullet.bulletAttr to javafx.fxml;
  exports application.bullet.bulletTypes;
  opens application.bullet.bulletTypes to javafx.fxml;
  exports application.bullet;
  opens application.bullet to javafx.fxml;
}