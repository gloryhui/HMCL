/*
 * Hello Minecraft! Launcher.
 * Copyright (C) 2013  huangyuhui <huanghongxun2008@126.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */
package org.jackhuang.hmcl.api.event.process;

import org.jackhuang.hmcl.api.IProcess;
import org.jackhuang.hmcl.api.event.SimpleEvent;

/**
 * This event gets fired when a JavaProcess is starting.
 * <br>
 * This event is fired on the {@link org.jackhuang.hmcl.api.HMCLApi#EVENT_BUS}
 * @param source {@link org.jackhuang.hmcl.util.sys.JavaProcessMonitor}
 * @param JavaProcess the starting JavaProcess.
 * @author huang
 */
public class JavaProcessStartingEvent extends SimpleEvent<IProcess> {
    
    public JavaProcessStartingEvent(Object source, IProcess value) {
        super(source, value);
    }
    
}
