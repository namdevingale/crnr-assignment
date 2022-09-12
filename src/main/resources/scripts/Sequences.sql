-- SEQUENCE: crnr_hcms.assignment_patient_id_seq

-- DROP SEQUENCE crnr_hcms.assignment_patient_id_seq;

CREATE SEQUENCE IF NOT EXISTS crnr_hcms.assignment_patient_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE crnr_hcms.assignment_patient_id_seq
    OWNER TO postgres;
	
	
	
-- SEQUENCE: crnr_hcms.assignment_address_id_seq

-- DROP SEQUENCE crnr_hcms.assignment_address_id_seq;

CREATE SEQUENCE IF NOT EXISTS crnr_hcms.assignment_address_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE crnr_hcms.assignment_address_id_seq
    OWNER TO postgres;
