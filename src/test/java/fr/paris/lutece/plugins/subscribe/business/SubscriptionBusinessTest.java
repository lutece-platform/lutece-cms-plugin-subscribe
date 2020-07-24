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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Tests for Subscription business classes
 */
public class SubscriptionBusinessTest extends LuteceTestCase
{
    private final static int IDABONNEMENT1 = 1;
    private final static String SUBSCRIPTIONPROVIDER1 = "SubscriptionProvider1";
    private final static String SUBSCRIPTIONPROVIDER2 = "SubscriptionProvider2";
    private final static String ACTIONKEY1 = "ActionKey1";
    private final static String ACTIONKEY2 = "ActionKey2";
    private final static String IDSUBSCRIBEDRESOURCE1 = "IdSubscribedResource1";
    private final static String IDSUBSCRIBEDRESOURCE2 = "IdSubscribedResource2";

    /**
     * Test the business package of this plugin
     */
    public void testBusiness( )
    {
        ISubscriptionDAO dao = new SubscriptionDAO( );
        Plugin plugin = PluginService.getPlugin( "subscribe" );
        // Initialize an object
        Subscription subscription = new Subscription( );
        subscription.setUserId( "0" );
        subscription.setIdSubscription( IDABONNEMENT1 );
        subscription.setSubscriptionProvider( SUBSCRIPTIONPROVIDER1 );
        subscription.setSubscriptionKey( ACTIONKEY1 );
        subscription.setIdSubscribedResource( IDSUBSCRIBEDRESOURCE1 );

        // Create test
        dao.insert( subscription, plugin );
        Subscription subscriptionStored = dao.load( subscription.getIdSubscription( ), plugin );
        assertNotNull( subscriptionStored );
        assertEquals( subscriptionStored.getIdSubscription( ), subscription.getIdSubscription( ) );
        assertEquals( subscriptionStored.getSubscriptionProvider( ), subscription.getSubscriptionProvider( ) );
        assertEquals( subscriptionStored.getSubscriptionKey( ), subscription.getSubscriptionKey( ) );
        assertEquals( subscriptionStored.getIdSubscribedResource( ), subscription.getIdSubscribedResource( ) );

        // Update test
        subscription.setSubscriptionProvider( SUBSCRIPTIONPROVIDER2 );
        subscription.setSubscriptionKey( ACTIONKEY2 );
        subscription.setIdSubscribedResource( IDSUBSCRIBEDRESOURCE2 );
        dao.store( subscription, plugin );
        subscriptionStored = dao.load( subscription.getIdSubscription( ), plugin );
        assertNotNull( subscriptionStored );
        assertEquals( subscriptionStored.getIdSubscription( ), subscription.getIdSubscription( ) );
        assertEquals( subscriptionStored.getSubscriptionProvider( ), subscription.getSubscriptionProvider( ) );
        assertEquals( subscriptionStored.getSubscriptionKey( ), subscription.getSubscriptionKey( ) );
        assertEquals( subscriptionStored.getIdSubscribedResource( ), subscription.getIdSubscribedResource( ) );

        // List test
        dao.selectSubscriptionsList( plugin );

        // Delete test
        dao.delete( subscription.getIdSubscription( ), plugin );
        subscriptionStored = dao.load( subscription.getIdSubscription( ), plugin );
        assertNull( subscriptionStored );
    }
}
