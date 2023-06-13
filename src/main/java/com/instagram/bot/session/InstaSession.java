package com.instagram.bot.session;

import cz.pumpitup.pn5.core.webdriver.Capability;
import cz.pumpitup.pn5.core.webdriver.ValueType;
import cz.pumpitup.pn5.web.WebApplication;

@Capability(key = "browserName", value = "chrome")
@Capability(key = "sessionTimeout", value = "1m")
@Capability(key = "timeout", value = "30s")
@Capability(key = "pn5:manualSession", value = "true", type = ValueType.BOOLEAN)
public interface InstaSession extends WebApplication {




}
