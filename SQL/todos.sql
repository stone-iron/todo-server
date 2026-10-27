-- 할 일 목록 테이블 (MySQL)
-- 실행 전: CREATE DATABASE IF NOT EXISTS aloha; USE aloha;

-- 터미널 기본 문자셋이 latin1 이어도 한글이 깨지지 않도록 지정
SET NAMES utf8mb4;

DROP TABLE IF EXISTS `todos`;

CREATE TABLE `todos` (
	`no`         BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
	`id`         VARCHAR(64) NOT NULL UNIQUE                     COMMENT 'UK',
	`name`       TEXT        NOT NULL                            COMMENT '할일',
	`status`     BOOLEAN     NOT NULL DEFAULT false              COMMENT '상태',
	`seq`        INT         NOT NULL DEFAULT 0                  COMMENT '순서',
	`created_at` TIMESTAMP   NOT NULL DEFAULT current_timestamp  COMMENT '등록일자',
	`updated_at` TIMESTAMP   NOT NULL DEFAULT current_timestamp  COMMENT '수정일자'
);

-- 샘플 데이터
INSERT INTO todos ( id, name, status, seq )
VALUES
	( UUID(), '할일1', TRUE, 1 ),
	( UUID(), '할일2', FALSE, 2 ),
	( UUID(), '할일3', TRUE, 3 ),
	( UUID(), '할일4', FALSE, 4 ),
	( UUID(), '할일5', TRUE, 5 ),
	( UUID(), '할일6', FALSE, 6 ),
	( UUID(), '할일7', TRUE, 7 ),
	( UUID(), '할일8', FALSE, 8 ),
	( UUID(), '할일9', TRUE, 9 ),
	( UUID(), '할일10', FALSE, 10 ),
	( UUID(), '할일11', TRUE, 1 ),
	( UUID(), '할일12', FALSE, 2 ),
	( UUID(), '할일13', TRUE, 3 ),
	( UUID(), '할일14', FALSE, 4 ),
	( UUID(), '할일15', TRUE, 5 ),
	( UUID(), '할일16', FALSE, 6 ),
	( UUID(), '할일17', TRUE, 7 ),
	( UUID(), '할일18', FALSE, 8 ),
	( UUID(), '할일19', TRUE, 9 ),
	( UUID(), '할일20', FALSE, 10 );
