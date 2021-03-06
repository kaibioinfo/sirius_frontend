/*
 *  This file is part of the SIRIUS library for analyzing MS and MS/MS data
 *
 *  Copyright (C) 2013-2015 Kai Dührkop
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with SIRIUS.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.unijena.bioinf.sirius;


import de.unijena.bioinf.FragmentationTreeConstruction.computation.FragmentationPatternAnalysis;
import de.unijena.bioinf.IsotopePatternAnalysis.IsotopePatternAnalysis;
import de.unijena.bioinf.babelms.json.JSONDocumentType;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Profile {

    public final FragmentationPatternAnalysis fragmentationPatternAnalysis;
    public final IsotopePatternAnalysis isotopePatternAnalysis;

    public Profile(String name) throws IOException {
        final JSONObject json = JSONDocumentType.getJSON("/profiles/" + name.toLowerCase() + ".json", name);
        final JSONDocumentType document = new JSONDocumentType();
        if (document.hasKeyInDictionary(json, "FragmentationPatternAnalysis")) this.fragmentationPatternAnalysis = FragmentationPatternAnalysis.loadFromProfile(document, json);
        else fragmentationPatternAnalysis=null;
        if (document.hasKeyInDictionary(json, "IsotopePatternAnalysis")) this.isotopePatternAnalysis = IsotopePatternAnalysis.loadFromProfile(document, json);
        else isotopePatternAnalysis=null;
    }

    public Profile(IsotopePatternAnalysis ms1, FragmentationPatternAnalysis ms2) {
        this.fragmentationPatternAnalysis = ms2;
        this.isotopePatternAnalysis = ms1;
    }

    public void writeToFile(String fileName) throws IOException  {
        writeToFile(new File(fileName));
    }

    public void writeToFile(File name) throws IOException {
        final FileWriter writer = new FileWriter(name);
        final JSONDocumentType json = new JSONDocumentType();
        final JSONObject obj = json.newDictionary();
        if (fragmentationPatternAnalysis != null) {
            fragmentationPatternAnalysis.writeToProfile(json, obj);
        }
        if (isotopePatternAnalysis != null) {
            isotopePatternAnalysis.writeToProfile(json, obj);
        }
        try {
            JSONDocumentType.writeJson(json, obj, writer);
        } finally {
            writer.close();
        }
    }

}
