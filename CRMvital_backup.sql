--
-- PostgreSQL database dump
--

\restrict iFaR3Am7kKQEKV2ziWm6coY3xlrc6c6jR3eBOH916RVmB2cAubPheOJxYToGelB

-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.6

-- Started on 2025-09-14 18:01:52

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 230 (class 1259 OID 16458)
-- Name: appointment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.appointment (
    id_appointment integer NOT NULL,
    id_patient integer,
    id_professional integer,
    id_service integer,
    date_hour timestamp without time zone,
    id_appointment_status integer,
    price numeric(10,2),
    id_clinic integer
);


ALTER TABLE public.appointment OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 16457)
-- Name: appointment_id_appointment_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.appointment_id_appointment_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.appointment_id_appointment_seq OWNER TO postgres;

--
-- TOC entry 4993 (class 0 OID 0)
-- Dependencies: 229
-- Name: appointment_id_appointment_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.appointment_id_appointment_seq OWNED BY public.appointment.id_appointment;


--
-- TOC entry 228 (class 1259 OID 16451)
-- Name: appointment_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.appointment_status (
    id_appointment_status integer NOT NULL,
    status_name character varying(50)
);


ALTER TABLE public.appointment_status OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16450)
-- Name: appointment_status_id_appointment_status_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.appointment_status_id_appointment_status_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.appointment_status_id_appointment_status_seq OWNER TO postgres;

--
-- TOC entry 4994 (class 0 OID 0)
-- Dependencies: 227
-- Name: appointment_status_id_appointment_status_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.appointment_status_id_appointment_status_seq OWNED BY public.appointment_status.id_appointment_status;


--
-- TOC entry 237 (class 1259 OID 24632)
-- Name: assistant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.assistant (
    id_assistant integer NOT NULL,
    id_user integer,
    first_name character varying(50) NOT NULL,
    first_last_name character varying(50) NOT NULL,
    second_last_name character varying(50),
    email character varying(100) NOT NULL,
    phone_number character varying(50)
);


ALTER TABLE public.assistant OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 24631)
-- Name: assistant_id_assistant_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.assistant_id_assistant_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.assistant_id_assistant_seq OWNER TO postgres;

--
-- TOC entry 4995 (class 0 OID 0)
-- Dependencies: 236
-- Name: assistant_id_assistant_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.assistant_id_assistant_seq OWNED BY public.assistant.id_assistant;


--
-- TOC entry 222 (class 1259 OID 16411)
-- Name: auth_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.auth_users (
    id_user integer NOT NULL,
    username character varying(255),
    password_auth character varying(255),
    id_rol integer,
    status boolean,
    id_clinic integer
);


ALTER TABLE public.auth_users OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16410)
-- Name: auth_users_id_user_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.auth_users_id_user_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.auth_users_id_user_seq OWNER TO postgres;

--
-- TOC entry 4996 (class 0 OID 0)
-- Dependencies: 221
-- Name: auth_users_id_user_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.auth_users_id_user_seq OWNED BY public.auth_users.id_user;


--
-- TOC entry 235 (class 1259 OID 24577)
-- Name: clinic; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clinic (
    id_clinic integer NOT NULL,
    clinic_name character varying(100) NOT NULL,
    address character varying(255),
    phone character varying(50),
    email character varying(100),
    timezone character varying(50) DEFAULT 'UTC'::character varying,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.clinic OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 24576)
-- Name: clinic_id_clinic_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.clinic_id_clinic_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.clinic_id_clinic_seq OWNER TO postgres;

--
-- TOC entry 4997 (class 0 OID 0)
-- Dependencies: 234
-- Name: clinic_id_clinic_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.clinic_id_clinic_seq OWNED BY public.clinic.id_clinic;


--
-- TOC entry 218 (class 1259 OID 16395)
-- Name: patient; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.patient (
    id_patient integer NOT NULL,
    name character varying(50),
    first_last_name character varying(50),
    second_last_name character varying(50),
    email character varying(155),
    phone_number character varying(15),
    birthdate date,
    sex character varying(50),
    address character varying(200),
    creation_date date,
    date_last_modified date,
    id_clinic integer
);


ALTER TABLE public.patient OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16394)
-- Name: patient_id_patient_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.patient_id_patient_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.patient_id_patient_seq OWNER TO postgres;

--
-- TOC entry 4998 (class 0 OID 0)
-- Dependencies: 217
-- Name: patient_id_patient_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.patient_id_patient_seq OWNED BY public.patient.id_patient;


--
-- TOC entry 232 (class 1259 OID 16485)
-- Name: predictions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.predictions (
    id_predictions integer NOT NULL,
    id_patient integer,
    prediction_date date,
    prediction_type character varying(100),
    score integer
);


ALTER TABLE public.predictions OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 16484)
-- Name: predictions_id_predictions_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.predictions_id_predictions_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.predictions_id_predictions_seq OWNER TO postgres;

--
-- TOC entry 4999 (class 0 OID 0)
-- Dependencies: 231
-- Name: predictions_id_predictions_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.predictions_id_predictions_seq OWNED BY public.predictions.id_predictions;


--
-- TOC entry 224 (class 1259 OID 16423)
-- Name: professional; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.professional (
    id_professional integer NOT NULL,
    id_user integer,
    name_professional character varying(255),
    first_last_name character varying(255),
    second_last_name character varying(255),
    specialty character varying(255),
    email character varying(255),
    phone_number character varying(255),
    id_clinic integer,
    id_card character varying(255)
);


ALTER TABLE public.professional OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16422)
-- Name: professional_id_professional_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.professional_id_professional_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.professional_id_professional_seq OWNER TO postgres;

--
-- TOC entry 5000 (class 0 OID 0)
-- Dependencies: 223
-- Name: professional_id_professional_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.professional_id_professional_seq OWNED BY public.professional.id_professional;


--
-- TOC entry 239 (class 1259 OID 24646)
-- Name: refresh_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.refresh_token (
    id bigint NOT NULL,
    expiry_date timestamp(6) without time zone,
    token character varying(255),
    user_id integer NOT NULL
);


ALTER TABLE public.refresh_token OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 24645)
-- Name: refresh_token_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.refresh_token ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.refresh_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 16404)
-- Name: rol; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rol (
    id_rol integer NOT NULL,
    rol_name character varying(255),
    id integer NOT NULL
);


ALTER TABLE public.rol OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16403)
-- Name: rol_id_rol_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rol_id_rol_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rol_id_rol_seq OWNER TO postgres;

--
-- TOC entry 5001 (class 0 OID 0)
-- Dependencies: 219
-- Name: rol_id_rol_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rol_id_rol_seq OWNED BY public.rol.id_rol;


--
-- TOC entry 233 (class 1259 OID 16496)
-- Name: rol_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.rol ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.rol_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 226 (class 1259 OID 16435)
-- Name: service; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.service (
    id_service integer NOT NULL,
    name_service character varying(100),
    description text,
    estimated_duration integer,
    price numeric(10,2),
    id_clinic integer
);


ALTER TABLE public.service OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16434)
-- Name: service_id_service_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.service_id_service_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.service_id_service_seq OWNER TO postgres;

--
-- TOC entry 5002 (class 0 OID 0)
-- Dependencies: 225
-- Name: service_id_service_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.service_id_service_seq OWNED BY public.service.id_service;


--
-- TOC entry 4799 (class 2604 OID 16461)
-- Name: appointment id_appointment; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment ALTER COLUMN id_appointment SET DEFAULT nextval('public.appointment_id_appointment_seq'::regclass);


--
-- TOC entry 4798 (class 2604 OID 16454)
-- Name: appointment_status id_appointment_status; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment_status ALTER COLUMN id_appointment_status SET DEFAULT nextval('public.appointment_status_id_appointment_status_seq'::regclass);


--
-- TOC entry 4805 (class 2604 OID 24635)
-- Name: assistant id_assistant; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assistant ALTER COLUMN id_assistant SET DEFAULT nextval('public.assistant_id_assistant_seq'::regclass);


--
-- TOC entry 4795 (class 2604 OID 16414)
-- Name: auth_users id_user; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auth_users ALTER COLUMN id_user SET DEFAULT nextval('public.auth_users_id_user_seq'::regclass);


--
-- TOC entry 4801 (class 2604 OID 24580)
-- Name: clinic id_clinic; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clinic ALTER COLUMN id_clinic SET DEFAULT nextval('public.clinic_id_clinic_seq'::regclass);


--
-- TOC entry 4793 (class 2604 OID 16398)
-- Name: patient id_patient; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.patient ALTER COLUMN id_patient SET DEFAULT nextval('public.patient_id_patient_seq'::regclass);


--
-- TOC entry 4800 (class 2604 OID 16488)
-- Name: predictions id_predictions; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.predictions ALTER COLUMN id_predictions SET DEFAULT nextval('public.predictions_id_predictions_seq'::regclass);


--
-- TOC entry 4796 (class 2604 OID 16426)
-- Name: professional id_professional; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.professional ALTER COLUMN id_professional SET DEFAULT nextval('public.professional_id_professional_seq'::regclass);


--
-- TOC entry 4794 (class 2604 OID 16407)
-- Name: rol id_rol; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rol ALTER COLUMN id_rol SET DEFAULT nextval('public.rol_id_rol_seq'::regclass);


--
-- TOC entry 4797 (class 2604 OID 16438)
-- Name: service id_service; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service ALTER COLUMN id_service SET DEFAULT nextval('public.service_id_service_seq'::regclass);


--
-- TOC entry 4819 (class 2606 OID 16463)
-- Name: appointment appointment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_pkey PRIMARY KEY (id_appointment);


--
-- TOC entry 4817 (class 2606 OID 16456)
-- Name: appointment_status appointment_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment_status
    ADD CONSTRAINT appointment_status_pkey PRIMARY KEY (id_appointment_status);


--
-- TOC entry 4825 (class 2606 OID 24639)
-- Name: assistant assistant_id_user_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assistant
    ADD CONSTRAINT assistant_id_user_key UNIQUE (id_user);


--
-- TOC entry 4827 (class 2606 OID 24637)
-- Name: assistant assistant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assistant
    ADD CONSTRAINT assistant_pkey PRIMARY KEY (id_assistant);


--
-- TOC entry 4811 (class 2606 OID 16416)
-- Name: auth_users auth_users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auth_users
    ADD CONSTRAINT auth_users_pkey PRIMARY KEY (id_user);


--
-- TOC entry 4823 (class 2606 OID 24587)
-- Name: clinic clinic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clinic
    ADD CONSTRAINT clinic_pkey PRIMARY KEY (id_clinic);


--
-- TOC entry 4807 (class 2606 OID 16402)
-- Name: patient patient_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.patient
    ADD CONSTRAINT patient_pkey PRIMARY KEY (id_patient);


--
-- TOC entry 4821 (class 2606 OID 16490)
-- Name: predictions predictions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.predictions
    ADD CONSTRAINT predictions_pkey PRIMARY KEY (id_predictions);


--
-- TOC entry 4813 (class 2606 OID 16428)
-- Name: professional professional_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.professional
    ADD CONSTRAINT professional_pkey PRIMARY KEY (id_professional);


--
-- TOC entry 4829 (class 2606 OID 24650)
-- Name: refresh_token refresh_token_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT refresh_token_pkey PRIMARY KEY (id);


--
-- TOC entry 4809 (class 2606 OID 16409)
-- Name: rol rol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id_rol);


--
-- TOC entry 4815 (class 2606 OID 16442)
-- Name: service service_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT service_pkey PRIMARY KEY (id_service);


--
-- TOC entry 4836 (class 2606 OID 24603)
-- Name: appointment fk_appointment_clinic; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_appointment_clinic FOREIGN KEY (id_clinic) REFERENCES public.clinic(id_clinic);


--
-- TOC entry 4842 (class 2606 OID 24640)
-- Name: assistant fk_assistant_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assistant
    ADD CONSTRAINT fk_assistant_user FOREIGN KEY (id_user) REFERENCES public.auth_users(id_user);


--
-- TOC entry 4830 (class 2606 OID 24593)
-- Name: patient fk_patient_clinic; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.patient
    ADD CONSTRAINT fk_patient_clinic FOREIGN KEY (id_clinic) REFERENCES public.clinic(id_clinic);


--
-- TOC entry 4837 (class 2606 OID 16464)
-- Name: appointment fk_patient_ref; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_patient_ref FOREIGN KEY (id_patient) REFERENCES public.patient(id_patient);


--
-- TOC entry 4841 (class 2606 OID 16491)
-- Name: predictions fk_patient_ref; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.predictions
    ADD CONSTRAINT fk_patient_ref FOREIGN KEY (id_patient) REFERENCES public.patient(id_patient);


--
-- TOC entry 4833 (class 2606 OID 24598)
-- Name: professional fk_professional_clinic; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.professional
    ADD CONSTRAINT fk_professional_clinic FOREIGN KEY (id_clinic) REFERENCES public.clinic(id_clinic);


--
-- TOC entry 4838 (class 2606 OID 16469)
-- Name: appointment fk_professional_ref; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_professional_ref FOREIGN KEY (id_professional) REFERENCES public.professional(id_professional);


--
-- TOC entry 4831 (class 2606 OID 16417)
-- Name: auth_users fk_rol_ref; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auth_users
    ADD CONSTRAINT fk_rol_ref FOREIGN KEY (id_rol) REFERENCES public.rol(id_rol);


--
-- TOC entry 4835 (class 2606 OID 24608)
-- Name: service fk_service_clinic; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT fk_service_clinic FOREIGN KEY (id_clinic) REFERENCES public.clinic(id_clinic);


--
-- TOC entry 4839 (class 2606 OID 16474)
-- Name: appointment fk_service_ref; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_service_ref FOREIGN KEY (id_service) REFERENCES public.service(id_service);


--
-- TOC entry 4840 (class 2606 OID 16479)
-- Name: appointment fk_status_appointment_ref; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_status_appointment_ref FOREIGN KEY (id_appointment_status) REFERENCES public.appointment_status(id_appointment_status);


--
-- TOC entry 4834 (class 2606 OID 16429)
-- Name: professional fk_user_ref; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.professional
    ADD CONSTRAINT fk_user_ref FOREIGN KEY (id_user) REFERENCES public.auth_users(id_user);


--
-- TOC entry 4832 (class 2606 OID 24588)
-- Name: auth_users fk_users_clinic; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.auth_users
    ADD CONSTRAINT fk_users_clinic FOREIGN KEY (id_clinic) REFERENCES public.clinic(id_clinic);


-- Completed on 2025-09-14 18:01:52

--
-- PostgreSQL database dump complete
--

\unrestrict iFaR3Am7kKQEKV2ziWm6coY3xlrc6c6jR3eBOH916RVmB2cAubPheOJxYToGelB

