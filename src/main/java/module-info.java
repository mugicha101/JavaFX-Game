module com.example.demo {
  requires javafx.controls;
  requires javafx.fxml;


  opens application to javafx.fxml;
  exports application;
  exports application.bullet.attr;
  opens application.bullet.attr to javafx.fxml;
  exports application.bullet.types;
  opens application.bullet.types to javafx.fxml;
  exports application.bullet.staging to javafx.fxml;
  opens application.bullet.staging;
  exports application.bullet;
  opens application.bullet to javafx.fxml;
  exports application.sprite;
  opens application.sprite to javafx.fxml;
  exports application.stats;
  opens application.stats to javafx.fxml;
}