/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.gov.pi.tce.publicacoes.controller.beans;

import java.lang.reflect.Field;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

import br.gov.pi.tce.publicacoes.modelo.Publicacao;

public class LazySorter implements Comparator<Publicacao> {

    private String sortField;
    
    private SortOrder sortOrder;
    
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public int compare(Publicacao publicacao1, Publicacao publicacao2) {
        try {
        	
        	Field field1 = publicacao1.getClass().getDeclaredField(this.sortField);
        	Field field2 = publicacao2.getClass().getDeclaredField(this.sortField);
        	field1.setAccessible(true);
        	field2.setAccessible(true);

        	Object value1 = field1.get(publicacao1);
            Object value2 = field2.get(publicacao2);

          //  Object value1 = Publicacao.class.getField(this.sortField).get(publicacao1);
           // Object value2 = Publicacao.class.getField(this.sortField).get(publicacao2);

            int value = ((Comparable)value1).compareTo(value2);
            
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
