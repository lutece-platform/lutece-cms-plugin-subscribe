/*
 * Copyright (c) 2002-2020, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.subscribe.business;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * This is the business class for subscriptions. Users can subscribe to resources (identified by their id) with a subscription key for a provider.<br />
 * The provider is used to prevent collisions between subscriptions lists of different plugins. The subscription key is used to differentiate several
 * subscription lists from a same provider. The subscribed resource id is used to configure subscriptions. <br />
 * Users are identified by their subscriber id. The subscriber id of a LuteceUser can be retrieved by calling the method
 * {@link fr.paris.lutece.plugins.subscribe.service.SubscriptionService#getIdSubscriberFromLuteceUser(fr.paris.lutece.portal.service.security.LuteceUser)
 * SubscriptionService#getIdSubscriberFromLuteceUser( LuteceUser )} <br />
 * Every string fields of this class be less than 255 characters long
 */
public class Subscription
{
    // Variables declarations

    @NotEmpty( message = "#i18n{portal.validation.message.notEmpty}" )
    private int _nIdSubscription;
    @NotEmpty( message = "#i18n{portal.validation.message.notEmpty}" )
    private String _strUserId;
    @NotEmpty( message = "#i18n{portal.validation.message.notEmpty}" )
    @Size( max = 255, message = "#i18n{portal.validation.message.sizeMax}" )
    private String _strSubscriptionProvider;
    @NotEmpty( message = "#i18n{portal.validation.message.notEmpty}" )
    @Size( max = 255, message = "#i18n{portal.validation.message.sizeMax}" )
    private String _strSubscriptionKey;
    @Size( max = 255, message = "#i18n{portal.validation.message.sizeMax}" )
    private String _strIdSubscribedResource;

    /**
     * Returns the technical id of the subscription
     * 
     * @return The technical id of the subscription
     */
    public int getIdSubscription( )
    {
        return _nIdSubscription;
    }

    /**
     * Sets the technical id of the subscription
     * 
     * @param nIdSubscription
     *            The technical id of the subscription
     */
    public void setIdSubscription( int nIdSubscription )
    {
        _nIdSubscription = nIdSubscription;
    }

    /**
     * Get the id of the subscriber. Subscribers id can be retrieved by calling the method
     * {@link fr.paris.lutece.plugins.subscribe.service.SubscriptionService#getIdSubscriberFromLuteceUser(fr.paris.lutece.portal.service.security.LuteceUser)
     * SubscriptionService#getIdSubscriberFromLuteceUser( LuteceUser )}
     * 
     * @return The id of the subscriber
     */
    public String getUserId( )
    {
        return _strUserId;
    }

    /**
     * Get the id of the subscriber. Subscribers id can be retrieved by calling the method
     * {@link fr.paris.lutece.plugins.subscribe.service.SubscriptionService#getIdSubscriberFromLuteceUser(fr.paris.lutece.portal.service.security.LuteceUser)
     * SubscriptionService#getIdSubscriberFromLuteceUser( LuteceUser )}
     * 
     * @param strUserId
     *            The id of the subscriber
     */
    public void setUserId( String strUserId )
    {
        this._strUserId = strUserId;
    }

    /**
     * Returns the provider of the subscription
     * 
     * @return The provider of the subscription
     */
    public String getSubscriptionProvider( )
    {
        return _strSubscriptionProvider;
    }

    /**
     * Sets the SubscriptionProvider
     * 
     * @param strSubscriptionProvider
     *            The SubscriptionProvider
     */
    public void setSubscriptionProvider( String strSubscriptionProvider )
    {
        _strSubscriptionProvider = strSubscriptionProvider;
    }

    /**
     * Returns the subscription key. The subscription key is used to differentiates several subscription lists of a same provider
     * 
     * @return The subscription key
     */
    public String getSubscriptionKey( )
    {
        return _strSubscriptionKey;
    }

    /**
     * Sets the subscription key. The subscription key is used to differentiates several subscription lists of a same provider
     * 
     * @param strSubscriptionKey
     *            The ActionKey
     */
    public void setSubscriptionKey( String strSubscriptionKey )
    {
        _strSubscriptionKey = strSubscriptionKey;
    }

    /**
     * Returns the id of the subscribed resource
     * 
     * @return The id of the subscribed resource
     */
    public String getIdSubscribedResource( )
    {
        return _strIdSubscribedResource;
    }

    /**
     * Sets the id of the subscribed resource
     * 
     * @param strIdSubscribedResource
     *            The id of the subscribed resource
     */
    public void setIdSubscribedResource( String strIdSubscribedResource )
    {
        _strIdSubscribedResource = strIdSubscribedResource;
    }
}
