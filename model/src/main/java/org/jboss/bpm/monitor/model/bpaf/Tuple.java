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
package org.jboss.bpm.monitor.model.bpaf;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 6, 2010
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name="BPAF_EVENT_DATA")
public class Tuple
{

  private long id;

  private String name;
  private String value;
  private Event event;

  @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name="EVENT_ID")
  @XmlTransient
  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event)
  {
    this.event = event;
  }

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Column(name = "TID")
  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  @Column(name = "NAME")
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Column(name = "VALUE")  
  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }
}
