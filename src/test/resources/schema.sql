DROP TABLE IF EXISTS todos;

CREATE TABLE todos (
	no         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
	id         VARCHAR(64) NOT NULL UNIQUE,
	name       TEXT        NOT NULL,
	status     BOOLEAN     NOT NULL DEFAULT false,
	seq        INT         NOT NULL DEFAULT 0,
	created_at TIMESTAMP   NOT NULL DEFAULT current_timestamp,
	updated_at TIMESTAMP   NOT NULL DEFAULT current_timestamp
);
