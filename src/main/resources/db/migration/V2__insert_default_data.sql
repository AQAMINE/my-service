-- ====================================================================
-- 1. INSERTION DES CATÉGORIES SYSTÈME PAR DÉFAUT (user_id IS NULL)
-- ====================================================================

INSERT INTO account_categories (id, name, slug, description) VALUES
(gen_random_uuid(), 'Social & Messaging', 'social_messaging', 'Réseaux sociaux, messageries et communication'),
(gen_random_uuid(), 'Hosting & Cloud', 'hosting_cloud', 'Hébergement web, VPS, bases de données et services cloud'),
(gen_random_uuid(), 'Developer & Tech', 'developer_tech', 'Gestionnaires de code, outils CI/CD et plateformes dev'),
(gen_random_uuid(), 'Freelance & Work', 'freelance_work', 'Plateformes de travail, facturation et productivité'),
(gen_random_uuid(), 'Payment & Banking', 'payment_banking', 'Services bancaires, paiements en ligne et cryptomonnaies'),
(gen_random_uuid(), 'Entertainment & Streaming', 'entertainment_streaming', 'Plateformes de vidéo, musique et jeux vidéo'),
(gen_random_uuid(), 'E-commerce & Shopping', 'ecommerce_shopping', 'Boutiques en ligne et marketplaces');


-- ====================================================================
-- 2. INSERTION DES PROVIDERS SYSTÈME PAR DÉFAUT (user_id IS NULL)
-- ====================================================================

-- Social & Messaging
INSERT INTO providers (id, name, slug, website_url, color, logo_url) VALUES
(gen_random_uuid(), 'Google / Gmail', 'google_gmail', 'https://accounts.google.com', '#EA4335', 'https://cdn.simpleicons.org/google/EA4335'),
(gen_random_uuid(), 'Instagram', 'instagram', 'https://instagram.com', '#E4405F', 'https://cdn.simpleicons.org/instagram/E4405F'),
(gen_random_uuid(), 'Facebook', 'facebook', 'https://facebook.com', '#1877F2', 'https://cdn.simpleicons.org/facebook/1877F2'),
(gen_random_uuid(), 'X / Twitter', 'x_twitter', 'https://x.com', '#000000', 'https://cdn.simpleicons.org/x/000000'),
(gen_random_uuid(), 'LinkedIn', 'linkedin', 'https://linkedin.com', '#0A66C2', 'https://cdn.simpleicons.org/linkedin/0A66C2'),
(gen_random_uuid(), 'WhatsApp', 'whatsapp', 'https://web.whatsapp.com', '#25D366', 'https://cdn.simpleicons.org/whatsapp/25D366'),
(gen_random_uuid(), 'Telegram', 'telegram', 'https://web.telegram.org', '#26A5E4', 'https://cdn.simpleicons.org/telegram/26A5E4'),
(gen_random_uuid(), 'Discord', 'discord', 'https://discord.com', '#5865F2', 'https://cdn.simpleicons.org/discord/5865F2'),
(gen_random_uuid(), 'TikTok', 'tiktok', 'https://tiktok.com', '#000000', 'https://cdn.simpleicons.org/tiktok/000000'),
(gen_random_uuid(), 'Reddit', 'reddit', 'https://reddit.com', '#FF4500', 'https://cdn.simpleicons.org/reddit/FF4500');

-- Developer & Tech
INSERT INTO providers (id, name, slug, website_url, color, logo_url) VALUES
(gen_random_uuid(), 'GitHub', 'github', 'https://github.com', '#181717', 'https://cdn.simpleicons.org/github/181717'),
(gen_random_uuid(), 'GitLab', 'gitlab', 'https://gitlab.com', '#FC6D26', 'https://cdn.simpleicons.org/gitlab/FC6D26'),
(gen_random_uuid(), 'Docker / Docker Hub', 'docker', 'https://hub.docker.com', '#2496ED', 'https://cdn.simpleicons.org/docker/2496ED'),
(gen_random_uuid(), 'Stack Overflow', 'stack_overflow', 'https://stackoverflow.com', '#F48024', 'https://cdn.simpleicons.org/stackoverflow/F48024'),
(gen_random_uuid(), 'OpenAI / ChatGPT', 'openai_chatgpt', 'https://chatgpt.com', '#412991', 'https://cdn.simpleicons.org/openai/412991'),
(gen_random_uuid(), 'Postman', 'postman', 'https://identity.getpostman.com', '#FF6C37', 'https://cdn.simpleicons.org/postman/FF6C37');

-- Hosting & Cloud
INSERT INTO providers (id, name, slug, website_url, color, logo_url) VALUES
(gen_random_uuid(), 'AWS (Amazon Web Services)', 'aws', 'https://aws.amazon.com', '#FF9900', 'https://cdn.simpleicons.org/amazonwebservices/FF9900'),
(gen_random_uuid(), 'DigitalOcean', 'digitalocean', 'https://cloud.digitalocean.com', '#0080FF', 'https://cdn.simpleicons.org/digitalocean/0080FF'),
(gen_random_uuid(), 'Hostinger', 'hostinger', 'https://hostinger.com', '#673DE6', 'https://cdn.simpleicons.org/hostinger/673DE6'),
(gen_random_uuid(), 'Vercel', 'vercel', 'https://vercel.com', '#000000', 'https://cdn.simpleicons.org/vercel/000000'),
(gen_random_uuid(), 'Cloudflare', 'cloudflare', 'https://dash.cloudflare.com', '#F38020', 'https://cdn.simpleicons.org/cloudflare/F38020'),
(gen_random_uuid(), 'OVHcloud', 'ovhcloud', 'https://ovh.com', '#000E9C', 'https://cdn.simpleicons.org/ovh/000E9C');

-- Freelance & Work
INSERT INTO providers (id, name, slug, website_url, color, logo_url) VALUES
(gen_random_uuid(), 'Fiverr', 'fiverr', 'https://fiverr.com', '#1DBF73', 'https://cdn.simpleicons.org/fiverr/1DBF73'),
(gen_random_uuid(), 'Upwork', 'upwork', 'https://upwork.com', '#14A800', 'https://cdn.simpleicons.org/upwork/14A800'),
(gen_random_uuid(), 'Notion', 'notion', 'https://notion.so', '#000000', 'https://cdn.simpleicons.org/notion/000000'),
(gen_random_uuid(), 'Trello', 'trello', 'https://trello.com', '#0052CC', 'https://cdn.simpleicons.org/trello/0052CC'),
(gen_random_uuid(), 'Slack', 'slack', 'https://slack.com', '#4A154B', 'https://cdn.simpleicons.org/slack/4A154B'),
(gen_random_uuid(), 'Zoom', 'zoom', 'https://zoom.us', '#0B5CFF', 'https://cdn.simpleicons.org/zoom/0B5CFF');

-- Payment & Banking
INSERT INTO providers (id, name, slug, website_url, color, logo_url) VALUES
(gen_random_uuid(), 'PayPal', 'paypal', 'https://paypal.com', '#003087', 'https://cdn.simpleicons.org/paypal/003087'),
(gen_random_uuid(), 'Stripe', 'stripe', 'https://dashboard.stripe.com', '#008CDD', 'https://cdn.simpleicons.org/stripe/008CDD'),
(gen_random_uuid(), 'Wise', 'wise', 'https://wise.com', '#163300', 'https://cdn.simpleicons.org/wise/163300'),
(gen_random_uuid(), 'Binance', 'binance', 'https://binance.com', '#F0B90B', 'https://cdn.simpleicons.org/binance/F0B90B'),
(gen_random_uuid(), 'Payoneer', 'payoneer', 'https://payoneer.com', '#FF4800', 'https://cdn.simpleicons.org/payoneer/FF4800');

-- Entertainment & Streaming
INSERT INTO providers (id, name, slug, website_url, color, logo_url) VALUES
(gen_random_uuid(), 'Netflix', 'netflix', 'https://netflix.com', '#E50914', 'https://cdn.simpleicons.org/netflix/E50914'),
(gen_random_uuid(), 'Spotify', 'spotify', 'https://spotify.com', '#1DB954', 'https://cdn.simpleicons.org/spotify/1DB954'),
(gen_random_uuid(), 'YouTube', 'youtube', 'https://youtube.com', '#FF0000', 'https://cdn.simpleicons.org/youtube/FF0000'),
(gen_random_uuid(), 'Twitch', 'twitch', 'https://twitch.tv', '#9146FF', 'https://cdn.simpleicons.org/twitch/9146FF'),
(gen_random_uuid(), 'Steam', 'steam', 'https://store.steampowered.com', '#000000', 'https://cdn.simpleicons.org/steam/000000');

-- E-commerce & Shopping
INSERT INTO providers (id, name, slug, website_url, color, logo_url) VALUES
(gen_random_uuid(), 'Amazon', 'amazon', 'https://amazon.com', '#FF9900', 'https://cdn.simpleicons.org/amazon/FF9900'),
(gen_random_uuid(), 'AliExpress', 'aliexpress', 'https://aliexpress.com', '#FF4747', 'https://cdn.simpleicons.org/aliexpress/FF4747'),
(gen_random_uuid(), 'eBay', 'ebay', 'https://ebay.com', '#E53238', 'https://cdn.simpleicons.org/ebay/E53238');
