/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.bpm.monitor.gui.server;

import org.jboss.bpm.monitor.gui.client.HistoryRecords;
import org.jboss.bpm.monitor.model.BPAFDataSource;
import org.jboss.bpm.monitor.model.DataSourceFactory;
import org.jboss.bpm.monitor.model.bpaf.Event;
import org.jboss.bpm.monitor.model.bpaf.State;
import org.jboss.bpm.monitor.model.metric.Timespan;
import org.jboss.bpm.monitor.model.metric.TimespanFactory;
import org.jboss.errai.bus.server.annotations.Service;

import java.util.*;

@Service
public class HistoryRecordsService implements HistoryRecords
{
    
    private BPAFDataSource dataSource;

    public HistoryRecordsService()
    {
        this.dataSource = DataSourceFactory.createDataSource();
    }

    public List<String> getProcessDefinitionKeys()
    {
        assertDataSource();
        return dataSource.getProcessDefinitions();
    }

    public List<String> getProcessInstanceKeys(String definition)
    {
        assertDataSource();
        return dataSource.getProcessInstances(definition);
    }

    public List<String> getActivityKeys(String instance)
    {
        assertDataSource();
        return dataSource.getActivityDefinitions(instance);
    }

    /**
     * resolve the timespan bounds for a given timestap
     * and queries for any occurance within that range.
     *
     * @param definitionKey
     * @param timestamp
     * @param timespan
     * @return a list of process instance id's
     */
    public Set<String> getCompletedInstances(String definitionKey, long timestamp, String timespan) {

        Set<String> result = new HashSet<String>();

        Timespan chartTimespan = TimespanFactory.fromValue(timespan);
        long[] bounds = TimespanFactory.getLeftBounds(chartTimespan, new Date(timestamp));

        System.out.println(new Date(bounds[0]) +" - "+ new Date(bounds[1]));
        
        List<Event> events = dataSource.getInstanceEvents(
                definitionKey,
                new Timespan(bounds[0], bounds[1], chartTimespan.getUnit(), "custom"),
                State.Closed_Completed                
        );
        
        for(Event e : events)
        {
            if(e.getEventDetails().getCurrentState().equals(State.Closed_Completed))
                result.add(e.getProcessInstanceID());
        }

        return result;
    }

    // catch hosted mode errors
    private void assertDataSource() {
        if(null==this.dataSource)
            throw new IllegalStateException("BPAFDataSource not initialized");

    }
}
