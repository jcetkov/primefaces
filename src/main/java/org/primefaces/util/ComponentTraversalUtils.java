/*
 * Copyright 2015 tandraschko.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.util;

import java.util.ArrayList;
import java.util.Iterator;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UniqueIdVendor;
import javax.faces.context.FacesContext;

public class ComponentTraversalUtils {

    public static <T> T closest(Class<T> type, UIComponent base) {
        UIComponent parent = base.getParent();

        while (parent != null) {
            if (type.isAssignableFrom(parent.getClass())) {
                return (T) parent;
            }

            parent = parent.getParent();
        }

        return null;
    }

    public static <T> T first(Class<T> type, UIComponent base) {
        T result = null;

        Iterator<UIComponent> kids = base.getFacetsAndChildren();
        while (kids.hasNext() && (result == null)) {
            UIComponent kid = (UIComponent) kids.next();
            if (type.isAssignableFrom(kid.getClass())) {
                result = (T) kid;
                break;
            }

            result = first(type, base);
            if (result != null) {
                break;
            }
        }

        return result;
    }
    
    public static <T> ArrayList<T> children(Class<T> type, UIComponent base) {
        
        ArrayList<T> result = new ArrayList<T>();
        
        Iterator<UIComponent> kids = base.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (type.isAssignableFrom(kid.getClass())) {
                result.add((T) kid);
            }
        }

        return result;
    }
    
    /**
     * Finds the first component with the given id (NOT clientId!).
     * 
     * @param id The id.
     * @param base The base component to start the traversal.
     * @return The component or null.
     */
    public static UIComponent firstWithId(String id, UIComponent base) {
        if (id.equals(base.getId())) {
            return base;
        }

        UIComponent result = null;

        Iterator<UIComponent> kids = base.getFacetsAndChildren();
        while (kids.hasNext() && (result == null)) {
            UIComponent kid = (UIComponent) kids.next();
            if (id.equals(kid.getId())) {
                result = kid;
                break;
            }
            result = firstWithId(id, kid);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    
    
    public static UIForm closestForm(FacesContext context, UIComponent component) {
        return closest(UIForm.class, component);
    }

    public static UniqueIdVendor closestUniqueIdVendor(UIComponent component) {
        return (UniqueIdVendor) closest(UniqueIdVendor.class, component);
    }

    public static UIComponent closestNamingContainer(UIComponent component) {
        return (UIComponent) closest(NamingContainer.class, component);
    }
}
