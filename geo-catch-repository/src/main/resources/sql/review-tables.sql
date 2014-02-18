SET NAMES utf8;

-- Create likes review table
CREATE TABLE IF NOT EXISTS like_review (
  imageId bigint(20) NOT NULL,
  deviceId varchar(40) NOT NULL,
  PRIMARY KEY (imageId, deviceId)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

-- Create dislikes review table
CREATE TABLE IF NOT EXISTS dislike_review (
  imageId bigint(20) NOT NULL,
  deviceId varchar(40) NOT NULL,
  PRIMARY KEY (imageId, deviceId)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

-- Create reports review table
CREATE TABLE IF NOT EXISTS report_review (
  imageId bigint(20) NOT NULL,
  deviceId varchar(40) NOT NULL,
  PRIMARY KEY (imageId, deviceId)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

commit;
