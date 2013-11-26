SET NAMES utf8;

CREATE TABLE IF NOT EXISTS domain_property (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  isDefault TINYINT(1) NOT NULL,
  item BIGINT(20) NOT NULL,
  locale VARCHAR(255) NULL DEFAULT NULL,
  type BIGINT(20) NOT NULL,
  value VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (id))
ENGINE = InnoDB
AUTO_INCREMENT = 3;

-- In case if table were created by JPA need to update value column encoding
ALTER TABLE domain_property CONVERT TO CHARACTER SET utf8
COLLATE utf8_general_ci;

commit;
