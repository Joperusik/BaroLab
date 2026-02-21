CREATE TABLE post_votes
(
    id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    post_id    UUID        NOT NULL,
    user_id    UUID        NOT NULL,
    value      INTEGER     NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_post_votes_post
        FOREIGN KEY (post_id)
            REFERENCES posts (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_post_votes_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT uk_post_votes_post_user
        UNIQUE (post_id, user_id)
);
