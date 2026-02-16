CREATE TYPE task_type_enum AS ENUM (
    'CREATED',
    'IN_PROGRESS',
    'COMPLETED',
    'FAILED',
    'DELAYED'
);

-- CREATE TYPE notification_type_enum AS ENUM (
--     'SINGLE',
--     'GROUP'
-- );

-- 1. Create independent tables first
CREATE TABLE users (
   id BIGINT PRIMARY KEY,
   username VARCHAR(255),
   first_name VARCHAR(255),
   last_name VARCHAR(255)
);

CREATE TABLE user_groups (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(255)
);

-- 2. Create tables with dependencies
CREATE TABLE task (
      id BIGSERIAL PRIMARY KEY,
      name VARCHAR(255),
      description TEXT,
      task_type task_type_enum NOT NULL,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      due_date TIMESTAMP,
      assigner_id BIGINT NOT NULL,
      assignee_user_id BIGINT,
      assignee_group_id BIGINT,
      CONSTRAINT fk_task_assigner FOREIGN KEY (assigner_id) REFERENCES users(id),
      CONSTRAINT fk_task_assignee_user FOREIGN KEY (assignee_user_id) REFERENCES users(id),
      CONSTRAINT fk_task_assignee_group FOREIGN KEY (assignee_group_id) REFERENCES user_groups(id)
);

-- CREATE TABLE notifications (
--    id BIGSERIAL PRIMARY KEY,
--    message TEXT,
--    notification_type notification_type_enum NOT NULL,
--    recipient_id BIGINT,
--    group_id BIGINT,
--    task_id BIGINT NOT NULL,
--    CONSTRAINT fk_notification_recipient FOREIGN KEY (recipient_id) REFERENCES users(id),
--    CONSTRAINT fk_notification_group FOREIGN KEY (group_id) REFERENCES user_groups(id),
--    CONSTRAINT fk_notification_task FOREIGN KEY (task_id) REFERENCES task(id)
-- );

-- 3. Create join tables for Many-to-Many relationships
CREATE TABLE user_group_membership (
   group_id BIGINT NOT NULL,
   user_id BIGINT NOT NULL,
   PRIMARY KEY (group_id, user_id),
   CONSTRAINT fk_membership_group FOREIGN KEY (group_id) REFERENCES user_groups(id),
   CONSTRAINT fk_membership_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- CREATE TABLE notification_reads (
--     notification_id BIGINT NOT NULL,
--     user_id BIGINT NOT NULL,
--     PRIMARY KEY (notification_id, user_id),
--     CONSTRAINT fk_read_notification FOREIGN KEY (notification_id) REFERENCES notifications(id),
--     CONSTRAINT fk_read_user FOREIGN KEY (user_id) REFERENCES users(id)
-- );

-- 4. Indexes
CREATE INDEX idx_task_assigner_id ON task(assigner_id);
CREATE INDEX idx_task_assignee_user_id ON task(assignee_user_id);
CREATE INDEX idx_task_assignee_group_id ON task(assignee_group_id);

-- CREATE INDEX idx_notifications_recipient_id ON notifications(recipient_id);
-- CREATE INDEX idx_notifications_task_id ON notifications(task_id);

CREATE INDEX idx_membership_group_id ON user_group_membership(group_id);
CREATE INDEX idx_membership_user_id ON user_group_membership(user_id);
