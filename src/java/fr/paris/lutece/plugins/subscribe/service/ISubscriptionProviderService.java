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
package fr.paris.lutece.plugins.subscribe.service;

import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.portal.service.security.LuteceUser;

import java.util.Locale;

/**
 * Interface of services that provide subscriptions to resources
 */
public interface ISubscriptionProviderService
{
    /**
     * Get the name of the subscription provider.
     * 
     * @return The name of the subscription provider
     */
    String getProviderName( );

    /**
     * Get the description of a subscription by its action key and the resource id associated with it
     * 
     * @param user
     *            The user
     * @param strSubscriptionKey
     *            The key of the subscription
     * @param strIdSubscribedResource
     *            The id of the subscribed resource
     * @param locale
     *            The locale to display the generated HTML in
     * @return An HTML string that describes the subscription
     */
    String getSubscriptionHtmlDescription( LuteceUser user, String strSubscriptionKey, String strIdSubscribedResource, Locale locale );

    String getSubscriptionHtmlDescriptionBis( LuteceUser user, String strSubscriptionKey, String strIdSubscribedResource, Locale locale, String userSub );

    /**
     * Check if a subscription can be automatically removed. If the subscription is removed, the method {@link #notifySubscriptionRemoval(Subscription)} of the
     * provider of the subscription will be called
     * 
     * @param user
     *            The user
     * @param strSubscriptionKey
     *            The subscription key
     * @param strIdSubscribedResource
     *            The id of the subscribed resource
     * @return True if the subscription can be removed, false otherwise
     */
    boolean isSubscriptionRemovable( LuteceUser user, String strSubscriptionKey, String strIdSubscribedResource );

    /**
     * Get the URL to modify the subscription
     * 
     * @param user
     *            The user
     * @param strSubscriptionKey
     *            The key of the subscription
     * @param strIdSubscribedResource
     *            The id of the subscribed resource
     * @return The URL to modify the subscription, or null if the subscription can not be modified
     */
    String getUrlModifySubscription( LuteceUser user, String strSubscriptionKey, String strIdSubscribedResource );

    /**
     * Notify the provider that a subscription associated with it has been removed
     * 
     * @param subscription
     *            The removed subscription
     */
    void notifySubscriptionRemoval( Subscription subscription );
}
