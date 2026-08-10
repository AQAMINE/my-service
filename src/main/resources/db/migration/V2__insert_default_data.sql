-- ====================================================================
-- 1. INSERTION DES CATÉGORIES SYSTÈME PAR DÉFAUT (user_id IS NULL)
-- ====================================================================

INSERT INTO account_categories (name, slug, description) VALUES
('Social & Messaging', 'social_messaging', 'Réseaux sociaux, messageries et communication'),
('Hosting & Cloud', 'hosting_cloud', 'Hébergement web, VPS, bases de données et services cloud'),
('Developer & Tech', 'developer_tech', 'Gestionnaires de code, outils CI/CD et plateformes dev'),
('Freelance & Work', 'freelance_work', 'Plateformes de travail, facturation et productivité'),
('Payment & Banking', 'payment_banking', 'Services bancaires, paiements en ligne et cryptomonnaies'),
('Entertainment & Streaming', 'entertainment_streaming', 'Plateformes de vidéo, musique et jeux vidéo'),
('E-commerce & Shopping', 'ecommerce_shopping', 'Boutiques en ligne et marketplaces');


-- ====================================================================
-- 2. INSERTION DES PROVIDERS SYSTÈME PAR DÉFAUT (user_id IS NULL)
-- ====================================================================

-- 📱 Social & Messaging
INSERT INTO providers (name, slug, website_url, color, logo_url) VALUES
('Google / Gmail', 'google_gmail', 'https://accounts.google.com', '#EA4335', 'https://cdn.simpleicons.org/google/EA4335'),
('Instagram', 'instagram', 'https://instagram.com', '#E4405F', 'https://cdn.simpleicons.org/instagram/E4405F'),
('Facebook', 'facebook', 'https://facebook.com', '#1877F2', 'https://cdn.simpleicons.org/facebook/1877F2'),
('X / Twitter', 'x_twitter', 'https://x.com', '#000000', 'https://cdn.simpleicons.org/x/000000'),
('LinkedIn', 'linkedin', 'https://linkedin.com', '#0A66C2', 'https://cdn.simpleicons.org/linkedin/0A66C2'),
('WhatsApp', 'whatsapp', 'https://web.whatsapp.com', '#25D366', 'https://cdn.simpleicons.org/whatsapp/25D366'),
('Telegram', 'telegram', 'https://web.telegram.org', '#26A5E4', 'https://cdn.simpleicons.org/telegram/26A5E4'),
('Discord', 'discord', 'https://discord.com', '#5865F2', 'https://cdn.simpleicons.org/discord/5865F2'),
('TikTok', 'tiktok', 'https://tiktok.com', '#000000', 'https://cdn.simpleicons.org/tiktok/000000'),
('Reddit', 'reddit', 'https://reddit.com', '#FF4500', 'https://cdn.simpleicons.org/reddit/FF4500');

-- 💻 Developer & Tech
INSERT INTO providers (name, slug, website_url, color, logo_url) VALUES
('GitHub', 'github', 'https://github.com', '#181717', 'https://cdn.simpleicons.org/github/181717'),
('GitLab', 'gitlab', 'https://gitlab.com', '#FC6D26', 'https://cdn.simpleicons.org/gitlab/FC6D26'),
('Docker / Docker Hub', 'docker', 'https://hub.docker.com', '#2496ED', 'https://cdn.simpleicons.org/docker/2496ED'),
('Stack Overflow', 'stack_overflow', 'https://stackoverflow.com', '#F48024', 'https://cdn.simpleicons.org/stackoverflow/F48024'),
('OpenAI / ChatGPT', 'openai_chatgpt', 'https://chatgpt.com', '#412991', 'https://cdn.simpleicons.org/openai/412991'),
('Postman', 'postman', 'https://identity.getpostman.com', '#FF6C37', 'https://cdn.simpleicons.org/postman/FF6C37');

-- ☁️ Hosting & Cloud
INSERT INTO providers (name, slug, website_url, color, logo_url) VALUES
('AWS (Amazon Web Services)', 'aws', 'https://aws.amazon.com', '#FF9900', 'https://cdn.simpleicons.org/amazonwebservices/FF9900'),
('DigitalOcean', 'digitalocean', 'https://cloud.digitalocean.com', '#0080FF', 'https://cdn.simpleicons.org/digitalocean/0080FF'),
('Hostinger', 'hostinger', 'https://hostinger.com', '#673DE6', 'https://cdn.simpleicons.org/hostinger/673DE6'),
('Vercel', 'vercel', 'https://vercel.com', '#000000', 'https://cdn.simpleicons.org/vercel/000000'),
('Cloudflare', 'cloudflare', 'https://dash.cloudflare.com', '#F38020', 'https://cdn.simpleicons.org/cloudflare/F38020'),
('OVHcloud', 'ovhcloud', 'https://ovh.com', '#000E9C', 'https://cdn.simpleicons.org/ovh/000E9C');

-- 💼 Freelance & Work
INSERT INTO providers (name, slug, website_url, color, logo_url) VALUES
('Fiverr', 'fiverr', 'https://fiverr.com', '#1DBF73', 'https://cdn.simpleicons.org/fiverr/1DBF73'),
('Upwork', 'upwork', 'https://upwork.com', '#14A800', 'https://cdn.simpleicons.org/upwork/14A800'),
('Notion', 'notion', 'https://notion.so', '#000000', 'https://cdn.simpleicons.org/notion/000000'),
('Trello', 'trello', 'https://trello.com', '#0052CC', 'https://cdn.simpleicons.org/trello/0052CC'),
('Slack', 'slack', 'https://slack.com', '#4A154B', 'https://cdn.simpleicons.org/slack/4A154B'),
('Zoom', 'zoom', 'https://zoom.us', '#0B5CFF', 'https://cdn.simpleicons.org/zoom/0B5CFF');

-- 💳 Payment & Banking
INSERT INTO providers (name, slug, website_url, color, logo_url) VALUES
('PayPal', 'paypal', 'https://paypal.com', '#003087', 'https://cdn.simpleicons.org/paypal/003087'),
('Stripe', 'stripe', 'https://dashboard.stripe.com', '#008CDD', 'https://cdn.simpleicons.org/stripe/008CDD'),
('Wise', 'wise', 'https://wise.com', '#163300', 'https://cdn.simpleicons.org/wise/163300'),
('Binance', 'binance', 'https://binance.com', '#F0B90B', 'https://cdn.simpleicons.org/binance/F0B90B'),
('Payoneer', 'payoneer', 'https://payoneer.com', '#FF4800', 'https://cdn.simpleicons.org/payoneer/FF4800');

-- 🎬 Entertainment & Streaming
INSERT INTO providers (name, slug, website_url, color, logo_url) VALUES
('Netflix', 'netflix', 'https://netflix.com', '#E50914', 'https://cdn.simpleicons.org/netflix/E50914'),
('Spotify', 'spotify', 'https://spotify.com', '#1DB954', 'https://cdn.simpleicons.org/spotify/1DB954'),
('YouTube', 'youtube', 'https://youtube.com', '#FF0000', 'https://cdn.simpleicons.org/youtube/FF0000'),
('Twitch', 'twitch', 'https://twitch.tv', '#9146FF', 'https://cdn.simpleicons.org/twitch/9146FF'),
('Steam', 'steam', 'https://store.steampowered.com', '#000000', 'https://cdn.simpleicons.org/steam/000000');

-- 🛒 E-commerce & Shopping
INSERT INTO providers (name, slug, website_url, color, logo_url) VALUES
('Amazon', 'amazon', 'https://amazon.com', '#FF9900', 'https://cdn.simpleicons.org/amazon/FF9900'),
('AliExpress', 'aliexpress', 'https://aliexpress.com', '#FF4747', 'https://cdn.simpleicons.org/aliexpress/FF4747'),
('eBay', 'ebay', 'https://ebay.com', '#E53238', 'https://cdn.simpleicons.org/ebay/E53238');