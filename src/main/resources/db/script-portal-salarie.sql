INSERT INTO ENTREPRISE (ID, CREATION_DATE, LAST_MODIFIED_DATE, ADRESSE1, ADRESSE2, CODEPOSTALE, PAYS, VILLE, CODEFISCAL, DESCRIPTION, EMAIL, NOM, NUMTEL, PHOTO, SITEWEB) VALUES (1, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'Niamey', 'Niamey', '11450', 'Niger', 'Niamey', '11111111', null, 'cyrille@yahoo.com', 'CYRILLE MBASSI', '8194617460', null, null);
INSERT INTO UTILISATEUR (ID, CREATION_DATE, LAST_MODIFIED_DATE, ADRESSE1, ADRESSE2, CODEPOSTALE, PAYS, VILLE, CLANG, DATEDENAISSANCE, EMAIL, MOTDEPASSE, NOM, PHOTO, PRENOM, IDENTREPRISE, VALID1, VALID2, VALID3, VALID4) VALUES (1, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'Niger', 'Niger', '11450', 'Niger', 'Niger', '001', null, 'cyrille@yahoo.com', '12345', 'MBASSI MASSOUKE', null, 'CYRILLE', 1, null, null, null, null);

INSERT INTO ROLES (ID, CREATION_DATE, LAST_MODIFIED_DATE, ACTIF, DATE_DEB, DATE_FIN, DROIT, MODULE, ROLENAME, IDUTILISATEUR) VALUES (1, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'O', TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2025-01-01 00:00:00', null, null, 'DRHL', 1);

INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (1, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'MOD_ABSCGE', 99, 'DELT', 1, 1, null, 'Bonjour,

Merci de bien vouloir traiter la demande d''absence / congé de $SENDER du $DATEDEBUT au $DATEFIN.

Cdlt,

RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (6, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'ACK_HABIL', 99, 'DELT', 1, 1, null, 'Bonjour,

Votre demande d''habilitation est soumise à la validation de $VALIDATOR.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (7, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'REJ_HABIL', 99, 'DELT', 1, 1, null, 'Bonjour,

Votre demande d''habilitation du $DATE a été rejetée.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (8, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'ANN_HABIL', 99, 'DELT', 1, 1, null, 'Bonjour,

La demande d''habilitation de $SENDER a été annulée par ce dernier.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (9, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'MOD_ATTEST', 99, 'DELT', 1, 1, null, 'Bonjour,

Merci de bien vouloir traiter la demande d''attestation de $SENDER.

Cdlt,

RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (10, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'ACK_ATTEST', 99, 'DELT', 1, 1, null, 'Bonjour,

Votre demande d''attestation est soumise à la validation de $VALIDATOR.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (11, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'REJ_ATTEST', 99, 'DELT', 1, 1, null, 'Bonjour,

Votre demande d''attestation pour la période du $DATE a été rejetée.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (12, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'ANN_ATTEST', 99, 'DELT', 1, 1, null, 'Bonjour,

La demande d''attestation de $SENDER a été annulée par ce dernier.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (13, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'ANN_MODINF', 99, 'DELT', 1, 1, null, 'Bonjour,

La demande de modification d''information de $SENDER a été annulée par ce dernier.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (14, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'REJ_MODINF', 99, 'DELT', 1, 1, null, 'Bonjour,

Votre demande de modification de l''information du $DATE de la fiche salarié a été rejetée.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (15, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'ACK_MODINF', 99, 'DELT', 1, 1, null, 'Bonjour,

Votre demande de modification de l''information de la fiche salarié est soumise a la validation de $VALIDATOR.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (16, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'MOD_MODINF', 99, 'DELT', 1, 1, null, 'Bonjour,

Merci de traiter la demande de modification de l''information de sa fiche salarié de $SENDER.

Cordialement,

RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (17, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'MOD_PRET', 99, 'DELT', 1, 1, null, 'Bonjour,

Merci de traiter la demande de prêt de $SENDER.

Cordialement,

RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (18, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'ACK_PRET', 99, 'DELT', 1, 1, null, 'Bonjour,

Votre demande de prêt est soumise à la validation de $VALIDATOR.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (19, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'REJ_PRET', 99, 'DELT', 1, 1, null, 'Bonjour,

Votre demande de prêt du $DATE a été rejetée.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (20, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'ANN_PRET', 99, 'DELT', 1, 1, null, 'Bonjour,

La demande de prêt de $SENDER a été annulée par ce dernier.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (21, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'VAL_DH', 99, 'DELT', 1, 1, null, 'cyrille.mbassi@yahoo.com', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (22, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'VAL_SCE', 99, 'DELT', 1, 1, null, 'cyrille.mbassi@yahoo.com', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (2, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'MOD_HABIL', 99, 'DELT', 1, 1, null, 'Bonjour,

Merci de traiter la demande d''habilitation de $SENDER.

Cordialement,

RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (3, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'ACK_ABSCGE', 99, 'DELT', 1, 1, null, 'Bonjour,

Votre demande d''absence / congé est soumise à la validation de $VALIDATOR.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (4, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'REJ_ABSCGE', 99, 'DELT', 1, 1, null, 'Bonjour,

Votre demande d''absence / congé pour la période du $DATEDEBUT au $DATEFIN a été rejetée.

Cdlt,
RH SONIBANK', null, null);
INSERT INTO PARAMDATA (ID, CREATION_DATE, LAST_MODIFIED_DATE, CACC, CTAB, DUTI, IDENTREPRISE, NUME, VALD, VALL, VALM, VALT) VALUES (5, TIMESTAMP '2023-01-01 00:00:00', TIMESTAMP '2023-01-01 00:00:00', 'ANN_ABSCGE', 99, 'DELT', 1, 1, null, 'Bonjour,

La demande d''absence / congé de $SENDER a été annulée par ce dernier.

Cdlt,
RH SONIBANK', null, null);


