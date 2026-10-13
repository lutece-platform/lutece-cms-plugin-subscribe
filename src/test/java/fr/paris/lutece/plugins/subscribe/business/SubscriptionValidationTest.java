/*
 * Copyright (c) 2002-2026, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.paris.lutece.test.LuteceTestCase;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the bean validation rules of Subscription
 */
public class SubscriptionValidationTest extends LuteceTestCase
{
    private static final String LONG_VALUE = "x".repeat( 256 );

    private ValidatorFactory _factory;
    private Validator _validator;

    /**
     * Builds the validator
     */
    @BeforeEach
    public void setUpValidator( )
    {
        _factory = Validation.buildDefaultValidatorFactory( );
        _validator = _factory.getValidator( );
    }

    /**
     * Closes the validator factory
     */
    @AfterEach
    public void tearDownValidator( )
    {
        _factory.close( );
    }

    /**
     * Refuses a subscription without user, provider and key
     */
    @Test
    public void testEmptyFieldsRefused( )
    {
        Subscription subscription = new Subscription( );
        subscription.setUserId( "" );
        subscription.setSubscriptionKey( "" );

        assertEquals( Set.of( "strUserId", "strSubscriptionProvider", "strSubscriptionKey" ), violations( subscription ), "empty fields" );
    }

    /**
     * Refuses values longer than 255 characters
     */
    @Test
    public void testTooLongFieldsRefused( )
    {
        Subscription subscription = validSubscription( );
        subscription.setSubscriptionProvider( LONG_VALUE );
        subscription.setSubscriptionKey( LONG_VALUE );
        subscription.setIdSubscribedResource( LONG_VALUE );

        assertEquals( Set.of( "strSubscriptionProvider", "strSubscriptionKey", "strIdSubscribedResource" ), violations( subscription ), "too long fields" );
    }

    /**
     * Accepts a complete subscription
     */
    @Test
    public void testValidSubscriptionAccepted( )
    {
        assertEquals( Set.of( ), violations( validSubscription( ) ), "valid subscription" );
    }

    /**
     * Builds a subscription that passes every rule
     *
     * @return the subscription
     */
    private static Subscription validSubscription( )
    {
        Subscription subscription = new Subscription( );
        subscription.setUserId( "user" );
        subscription.setSubscriptionProvider( "provider" );
        subscription.setSubscriptionKey( "key" );
        subscription.setIdSubscribedResource( "resource" );
        return subscription;
    }

    /**
     * Validates a subscription
     *
     * @param subscription
     *            the subscription
     * @return the names of the refused fields, without their prefix
     */
    private Set<String> violations( Subscription subscription )
    {
        Set<String> setFields = new TreeSet<>( );
        for ( ConstraintViolation<Subscription> violation : _validator.validate( subscription ) )
        {
            setFields.add( violation.getPropertyPath( ).toString( ).substring( 1 ) );
        }
        return setFields;
    }
}
