-- Table: crnr_hcms.assignment_patient

-- DROP TABLE IF EXISTS crnr_hcms.assignment_patient;

CREATE TABLE IF NOT EXISTS crnr_hcms.assignment_patient
(
    patient_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_date timestamp without time zone NOT NULL,
    is_deleted boolean NOT NULL,
    updated_date timestamp without time zone NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    date_of_birth timestamp without time zone NOT NULL,
    gender character varying(255) COLLATE pg_catalog."default" NOT NULL,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    primary_contact_country_code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    primary_contact_number bigint NOT NULL,
    secondary_contact_country_code character varying(255) COLLATE pg_catalog."default",
    secondary_contact_number bigint,
    CONSTRAINT assignment_patient_pkey PRIMARY KEY (patient_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS crnr_hcms.assignment_patient

    OWNER to postgres;	



-- Table: crnr_hcms.assignment_address

-- DROP TABLE IF EXISTS crnr_hcms.assignment_address;

CREATE TABLE IF NOT EXISTS crnr_hcms.assignment_address
(
    address_id bigint NOT NULL DEFAULT nextval('crnr_hcms.assignment_address_id_seq'::regclass),
    address_type character varying(255) COLLATE pg_catalog."default" NOT NULL,
    address_line_one character varying(255) COLLATE pg_catalog."default" NOT NULL,
    address_line_two character varying(255) COLLATE pg_catalog."default",
    city character varying(255) COLLATE pg_catalog."default" NOT NULL,
    state character varying(255) COLLATE pg_catalog."default" NOT NULL,
    country character varying(255) COLLATE pg_catalog."default" NOT NULL,
    pin_code bigint NOT NULL,
    patient_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT assignment_address_pkey PRIMARY KEY (address_id),
    CONSTRAINT fk_assignment_patient_id FOREIGN KEY (patient_id)
        REFERENCES crnr_hcms.assignment_patient (patient_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS crnr_hcms.assignment_address
    OWNER to postgres;	
