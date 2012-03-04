CREATE TABLE IF NOT EXISTS `gatekeeper_cidr` (
  `cidr` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT 'CIDR',
  `start` int(10) unsigned NOT NULL COMMENT 'Start range',
  `end` int(11) unsigned NOT NULL COMMENT 'End range',
  `comment` varchar(1024) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Comment',
  `type` enum('whitelist','blacklist') COLLATE utf8_unicode_ci NOT NULL DEFAULT 'whitelist' COMMENT 'Type',
  PRIMARY KEY (`cidr`,`type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;