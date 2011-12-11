CREATE TABLE IF NOT EXISTS `whitelist_player` (
  `player` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Player name',
  `comment` varchar(1024) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Comment',
  `type` enum('whitelist','blacklist') COLLATE utf8_unicode_ci NOT NULL COMMENT 'Type',
  `expire` int(10) unsigned NOT NULL COMMENT 'Expire date',
  PRIMARY KEY (`player`,`type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;