CREATE TABLE open_pension_fund (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  opf_name varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  opf_date date NOT NULL,
  opf_number_of_members bigint,
  opf_net_assets bigint,
  opf_total_number_of_accounts bigint,
  opf_inactive_number_of_accounts bigint,
  opf_contr_amount bigint,
  opf_contr_number bigint,
  opf_contr_interests bigint,
  opf_contr_average_basis bigint,
  PRIMARY KEY (id),
  UNIQUE KEY opf_uk (opf_name,opf_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE instrument (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  inst_identifier varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  inst_name varchar(1024) COLLATE utf8_unicode_ci NOT NULL,
  inst_description varchar(4096) COLLATE utf8_unicode_ci,
  PRIMARY KEY (id),
  UNIQUE KEY goi_uk (inst_identifier)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE investment (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  inv_opf_id bigint(20) NOT NULL,
  inv_instrument_id bigint(20) NOT NULL,
  inv_value  bigint NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY inv_uk (inv_opf_id, inv_instrument_id),
  CONSTRAINT inv_opf_fk FOREIGN KEY (inv_opf_id) REFERENCES open_pension_fund (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT inv_instrument_fk FOREIGN KEY (inv_instrument_id) REFERENCES instrument (id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

drop table investment;
drop table instrument;
drop table open_pension_fund;

select id,inst_name,inst_description
from instrument
INTO OUTFILE '/tmp/instruments.csv'
FIELDS TERMINATED BY '#' ENCLOSED BY '"' LINES TERMINATED BY '\n';