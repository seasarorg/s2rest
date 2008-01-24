/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.php
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */

/*
 * Request.java
 *
 * Created on September 27, 2007, 5:39 PM
 *
 */

package javax.ws.rs.core;

import java.util.Date;
import java.util.List;

/**
 * An injectable helper for request processing
 */
public interface Request {
    
    /**
     * Select the representation variant that best matches the request. More
     * explicit variants are chosen ahead of less explicit ones. A vary header
     * is computed from the supplied list and automatically added to the 
     * response.
     * 
     * @param variants a list of Variant that describe all of the
     * available representation variants.
     * @return the variant that best matches the request.
     * @see Variant.VariantListBuilder
     * @throws IllegalArgumentException if variants is empty
     */
    Variant selectVariant(List<Variant> variants) throws IllegalArgumentException;
    
    /**
     * Evaluate request preconditions based on the passed in value.
     * 
     * @param eTag an ETag for the current state of the resource
     * @return null if the preconditions are met or a Response that should be returned if the
     * preconditions are not met.
     */
    Response evaluatePreconditions(EntityTag eTag);

    /**
     * Evaluate request preconditions based on the passed in value.
     * 
     * @param lastModified a date that specifies the modification date of the resource
     * @return null if the preconditions are met or a Response that should be returned if the
     * preconditions are not met.
     */
    Response evaluatePreconditions(Date lastModified);
    
    /**
     * Evaluate request preconditions based on the passed in value.
     * 
     * @param lastModified a date that specifies the modification date of the resource
     * @param eTag an ETag for the current state of the resource
     * @return null if the preconditions are met or a Response that should be returned if the
     * preconditions are not met.
     */
    Response evaluatePreconditions(Date lastModified, EntityTag eTag);
}
