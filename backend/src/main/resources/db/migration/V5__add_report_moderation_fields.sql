ALTER TABLE reports
    ADD COLUMN moderated_by_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    ADD COLUMN moderation_reason VARCHAR(500),
    ADD COLUMN moderated_at TIMESTAMP WITH TIME ZONE;

CREATE INDEX idx_reports_moderated_by_id ON reports(moderated_by_id);
