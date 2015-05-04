CREATE TABLE open_pension_fund (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  opf_name varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  opf_date date NOT NULL,
  opf_number_of_members int(11),
  opf_total_number_of_accounts int(11),
  opf_inactive_number_of_accounts int(11),
  opf_contr_amount int(11),
  opf_contr_number int(11),
  opf_contr_interests int(11),
  opf_contr_average_basis int(11),
  PRIMARY KEY (id),
  UNIQUE KEY opf_uk (opf_name,opf_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

