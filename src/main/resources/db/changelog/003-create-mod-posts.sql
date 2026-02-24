CREATE TABLE mod_posts
(
    post_id UUID PRIMARY KEY,
    external_id BIGINT NOT NULL UNIQUE,
    subscriptions_count INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT fk_mod_posts_post
        FOREIGN KEY (post_id)
            REFERENCES posts (id)
            ON DELETE CASCADE
);

CREATE TABLE mod_subscribe_clicks
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mod_post_id UUID NOT NULL,
    subject_key TEXT NOT NULL,
    clicks_count INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_click_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_mod_clicks_mod_post
        FOREIGN KEY (mod_post_id)
            REFERENCES mod_posts (post_id)
            ON DELETE CASCADE,

    CONSTRAINT uk_mod_clicks_mod_subject
        UNIQUE (mod_post_id, subject_key)
);
