CREATE TABLE open_pension_fund (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  opf_name varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  opf_date date NOT NULL,
  opf_number_of_members bigint,
  opf_total_number_of_accounts bigint,
  opf_inactive_number_of_accounts bigint,
  opf_contr_amount bigint,
  opf_contr_number bigint,
  opf_contr_interests bigint,
  opf_contr_average_basis bigint,
  PRIMARY KEY (id),
  UNIQUE KEY opf_uk (opf_name,opf_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

