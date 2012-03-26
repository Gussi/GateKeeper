CREATE TABLE IF NOT EXISTS `gatekeeper_player` (
  `player` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Player name',
  `comment` varchar(1024) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Comment',
  `source` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Source',
  PRIMARY KEY (`player`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;