ALTER TABLE mod_subscribe_clicks RENAME TO mod_transition_clicks;

ALTER TABLE mod_transition_clicks
    RENAME CONSTRAINT uk_mod_clicks_mod_subject TO uk_mod_transitions_mod_subject;

ALTER TABLE mod_posts
    RENAME COLUMN subscriptions_count TO popularity;
