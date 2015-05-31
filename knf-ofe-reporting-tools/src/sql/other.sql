select id,inst_name,inst_description
from instrument
INTO OUTFILE '/tmp/instruments.csv'
FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n';

select count(*) from open_pension_fund union select count(*) from instrument union select count(*) from investment;

select count(*) from open_pension_fund where opf_number_of_members is null or opf_number_of_members=0
union select count(*) from open_pension_fund where opf_net_assets is null or opf_net_assets=0
union select count(*) from open_pension_fund where opf_total_number_of_accounts is null or opf_total_number_of_accounts =0
union select count(*) from open_pension_fund where opf_contr_amount is null or opf_contr_amount = 0;