package com.base.game;

import java.io.File;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.base.game.character.gender.AndrogynousIdentification;
import com.base.game.character.gender.Gender;
import com.base.game.character.gender.GenderPronoun;
import com.base.game.character.race.FurryPreference;
import com.base.game.character.race.Race;
import com.base.game.dialogue.eventLog.EventLogEntryEncyclopediaUnlock;
import com.base.game.inventory.clothing.AbstractClothingType;
import com.base.game.inventory.clothing.ClothingType;
import com.base.game.inventory.item.AbstractItemType;
import com.base.game.inventory.item.ItemType;
import com.base.game.inventory.weapon.AbstractWeaponType;
import com.base.game.inventory.weapon.WeaponType;
import com.base.main.Main;

/**
 * @since 0.1.0
 * @version 0.1.84
 * @author Innoxia
 */
public class Properties implements Serializable {
	private static final long serialVersionUID = 1L;
	public String lastSaveLocation = "", nameColour = "", name = "", race = "", quest = "", versionNumber="";
	public int fontSize = 18, level = 1, money = 0, humanEncountersLevel = 1, multiBreasts = 1;
	public boolean lightTheme = false, overwriteWarning = true, fadeInText=false,
			furryTailPenetrationContent = false,
			nonConContent = false,
			incestContent = false,
			forcedTransformationContent = false,
			facialHairContent = false,
			pubicHairContent = false,
			bodyHairContent = false,
			
			newWeaponDiscovered = false,
			newClothingDiscovered = false,
			newItemDiscovered = false,
			newRaceDiscovered = false;
	
	public AndrogynousIdentification androgynousIdentification = AndrogynousIdentification.CLOTHING_FEMININE;

	public Map<KeyboardAction, KeyCodeWithModifiers> hotkeyMapPrimary, hotkeyMapSecondary;
	
	public Map<GenderPronoun, String> genderPronounFemale, genderPronounMale;
	
	public Map<Gender, Integer> genderPreferencesMap;

	public Map<Race, FurryPreference> raceFemininePreferencesMap, raceMasculinePreferencesMap;
	
	// Discoveries:
	private Set<AbstractItemType> itemsDiscovered;
	private Set<AbstractWeaponType> weaponsDiscovered;
	private Set<AbstractClothingType> clothingDiscovered;
	private Set<Race> racesDiscovered, racesAdvancedKnowledge;

	public Properties() {
		hotkeyMapPrimary = new EnumMap<>(KeyboardAction.class);
		hotkeyMapSecondary = new EnumMap<>(KeyboardAction.class);

		for (KeyboardAction ka : KeyboardAction.values()) {
			hotkeyMapPrimary.put(ka, ka.getPrimaryDefault());
			hotkeyMapSecondary.put(ka, ka.getSecondaryDefault());
		}
		
		genderPronounFemale = new EnumMap<>(GenderPronoun.class);
		genderPronounMale = new EnumMap<>(GenderPronoun.class);

		for (GenderPronoun gp : GenderPronoun.values()) {
			genderPronounFemale.put(gp, gp.getFeminine());
			genderPronounMale.put(gp, gp.getMasculine());
		}
		
		genderPreferencesMap = new EnumMap<>(Gender.class);
		for(Gender g : Gender.values()) {
			genderPreferencesMap.put(g, g.getGenderPreferenceDefault().getValue());
		}
		
		raceFemininePreferencesMap = new EnumMap<>(Race.class);
		raceMasculinePreferencesMap = new EnumMap<>(Race.class);
		for(Race r : Race.values()) {
			raceFemininePreferencesMap.put(r, FurryPreference.NORMAL);
			raceMasculinePreferencesMap.put(r, FurryPreference.NORMAL);
		}
		
		itemsDiscovered = new HashSet<>();
		weaponsDiscovered = new HashSet<>();
		clothingDiscovered = new HashSet<>();
		racesDiscovered = new HashSet<>();
		racesAdvancedKnowledge = new HashSet<>();
	}
	
	public void savePropertiesAsXML(){
		try {
		
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element properties = doc.createElement("properties");
			doc.appendChild(properties);

			// Previous save information:
			Element previousSave = doc.createElement("previousSave");
			properties.appendChild(previousSave);
			createXMLElementWithValue(doc, previousSave, "location", lastSaveLocation);
			createXMLElementWithValue(doc, previousSave, "nameColour", nameColour);
			createXMLElementWithValue(doc, previousSave, "name", name);
			createXMLElementWithValue(doc, previousSave, "race", race);
			createXMLElementWithValue(doc, previousSave, "quest", quest);
			createXMLElementWithValue(doc, previousSave, "level", String.valueOf(level));
			createXMLElementWithValue(doc, previousSave, "money", String.valueOf(money));
			createXMLElementWithValue(doc, previousSave, "versionNumber", Main.VERSION_NUMBER);
			
			// Game settings:
			Element settings = doc.createElement("settings");
			properties.appendChild(settings);
			createXMLElementWithValue(doc, settings, "fontSize", String.valueOf(fontSize));
			createXMLElementWithValue(doc, settings, "lightTheme", String.valueOf(lightTheme));
			createXMLElementWithValue(doc, settings, "furryTailPenetrationContent", String.valueOf(furryTailPenetrationContent));
			createXMLElementWithValue(doc, settings, "nonConContent", String.valueOf(nonConContent));
			createXMLElementWithValue(doc, settings, "incestContent", String.valueOf(incestContent));
			createXMLElementWithValue(doc, settings, "forcedTransformationContent", String.valueOf(forcedTransformationContent));
			createXMLElementWithValue(doc, settings, "facialHairContent", String.valueOf(facialHairContent));
			createXMLElementWithValue(doc, settings, "pubicHairContent", String.valueOf(pubicHairContent));
			createXMLElementWithValue(doc, settings, "bodyHairContent", String.valueOf(bodyHairContent));
			createXMLElementWithValue(doc, settings, "overwriteWarning", String.valueOf(overwriteWarning));
			createXMLElementWithValue(doc, settings, "fadeInText", String.valueOf(fadeInText));
			createXMLElementWithValue(doc, settings, "androgynousIdentification", String.valueOf(androgynousIdentification));
			createXMLElementWithValue(doc, settings, "humanEncountersLevel", String.valueOf(humanEncountersLevel));
			createXMLElementWithValue(doc, settings, "multiBreasts", String.valueOf(multiBreasts));

			createXMLElementWithValue(doc, settings, "newWeaponDiscovered", String.valueOf(newWeaponDiscovered));
			createXMLElementWithValue(doc, settings, "newClothingDiscovered", String.valueOf(newClothingDiscovered));
			createXMLElementWithValue(doc, settings, "newItemDiscovered", String.valueOf(newItemDiscovered));
			createXMLElementWithValue(doc, settings, "newRaceDiscovered", String.valueOf(newRaceDiscovered));
			
			
			// Game key binds:
			Element keyBinds = doc.createElement("keyBinds");
			properties.appendChild(keyBinds);
			for (KeyboardAction ka : KeyboardAction.values()) {
				Element element = doc.createElement("binding");
				keyBinds.appendChild(element);
				
				Attr bindName = doc.createAttribute("bindName");
				bindName.setValue(ka.toString());
				element.setAttributeNode(bindName);
				
				Attr primaryBind = doc.createAttribute("primaryBind");
				if(hotkeyMapPrimary.get(ka)!=null)
					primaryBind.setValue(hotkeyMapPrimary.get(ka).toString());
				else
					primaryBind.setValue("");
				element.setAttributeNode(primaryBind);
				
				Attr secondaryBind = doc.createAttribute("secondaryBind");
				if(hotkeyMapSecondary.get(ka)!=null)
					secondaryBind.setValue(hotkeyMapSecondary.get(ka).toString());
				else
					secondaryBind.setValue("");
				element.setAttributeNode(secondaryBind);
			}
			
			// Gender pronouns:
			Element pronouns = doc.createElement("genderPronouns");
			properties.appendChild(pronouns);
			for (GenderPronoun gp : GenderPronoun.values()) {
				Element element = doc.createElement("pronoun");
				pronouns.appendChild(element);
				
				Attr pronounName = doc.createAttribute("pronounName");
				pronounName.setValue(gp.toString());
				element.setAttributeNode(pronounName);
				
				Attr feminineValue = doc.createAttribute("feminineValue");
				if(genderPronounFemale.get(gp)!=null)
					feminineValue.setValue(genderPronounFemale.get(gp));
				else
					feminineValue.setValue(gp.getFeminine());
				element.setAttributeNode(feminineValue);
				
				Attr masculineValue = doc.createAttribute("masculineValue");
				if(genderPronounMale.get(gp)!=null)
					masculineValue.setValue(genderPronounMale.get(gp));
				else
					masculineValue.setValue(gp.getMasculine());
				element.setAttributeNode(masculineValue);
			}
			
			// Gender preferences:
			Element genderPreferences = doc.createElement("genderPreferences");
			properties.appendChild(genderPreferences);
			for (Gender g : Gender.values()) {
				Element element = doc.createElement("preference");
				genderPreferences.appendChild(element);
				
				Attr gender = doc.createAttribute("gender");
				gender.setValue(g.toString());
				element.setAttributeNode(gender);
				
				Attr value = doc.createAttribute("value");
				value.setValue(String.valueOf(genderPreferencesMap.get(g).intValue()));
				element.setAttributeNode(value);
			}
			
			// Race preferences:
			Element racePreferences = doc.createElement("furryPreferences");
			properties.appendChild(racePreferences);
			for (Race r : Race.values()) {
				Element element = doc.createElement("preferenceFeminine");
				racePreferences.appendChild(element);
				
				Attr race = doc.createAttribute("race");
				race.setValue(r.toString());
				element.setAttributeNode(race);
				
				Attr preference = doc.createAttribute("preference");
				preference.setValue(raceFemininePreferencesMap.get(r).toString());
				element.setAttributeNode(preference);
				
				element = doc.createElement("preferenceMasculine");
				racePreferences.appendChild(element);
				
				race = doc.createAttribute("race");
				race.setValue(r.toString());
				element.setAttributeNode(race);
				
				preference = doc.createAttribute("preference");
				preference.setValue(raceMasculinePreferencesMap.get(r).toString());
				element.setAttributeNode(preference);
			}
			
			// Discoveries:
			Element itemsDiscovered = doc.createElement("itemsDiscovered");
			properties.appendChild(itemsDiscovered);
			for (AbstractItemType itemType : this.itemsDiscovered) {
				Element element = doc.createElement("itemType");
				itemsDiscovered.appendChild(element);
				
				Attr hash = doc.createAttribute("id");
				hash.setValue(itemType.getId());
				element.setAttributeNode(hash);
			}
			
			Element weaponsDiscovered = doc.createElement("weaponsDiscovered");
			properties.appendChild(weaponsDiscovered);
			for (AbstractWeaponType weaponType : this.weaponsDiscovered) {
				Element element = doc.createElement("weaponType");
				weaponsDiscovered.appendChild(element);
				
				Attr hash = doc.createAttribute("id");
				hash.setValue(weaponType.getId());
				element.setAttributeNode(hash);
			}
			
			Element clothingDiscovered = doc.createElement("clothingDiscovered");
			properties.appendChild(clothingDiscovered);
			for (AbstractClothingType clothingType : this.clothingDiscovered) {
				Element element = doc.createElement("clothingType");
				clothingDiscovered.appendChild(element);
				
				Attr hash = doc.createAttribute("id");
				hash.setValue(clothingType.getId());
				element.setAttributeNode(hash);
			}
			
			Element racesDiscovered = doc.createElement("racesDiscovered");
			properties.appendChild(racesDiscovered);
			for (Race race : Race.values()) {
				Element element = doc.createElement("raceDiscovery");
				racesDiscovered.appendChild(element);
				
				Attr discovered = doc.createAttribute("race");
				discovered.setValue(race.toString());
				element.setAttributeNode(discovered);
				
				discovered = doc.createAttribute("discovered");
				discovered.setValue(String.valueOf(this.racesDiscovered.contains(race)));
				element.setAttributeNode(discovered);
				
				discovered = doc.createAttribute("advancedKnowledge");
				discovered.setValue(String.valueOf(this.racesAdvancedKnowledge.contains(race)));
				element.setAttributeNode(discovered);
			}
			
			
			// Write out to properties.xml:
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("data/properties.xml"));
		
			transformer.transform(source, result);
		
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}
	
	private void createXMLElementWithValue(Document doc, Element parentElement, String elementName, String value){
		Element element = doc.createElement(elementName);
		parentElement.appendChild(element);
		Attr attr = doc.createAttribute("value");
		attr.setValue(value);
		element.setAttributeNode(attr);
	}
	
	public void loadPropertiesFromXML(){
		
		if (new File("data/properties.xml").exists())
			try {
				File propertiesXML = new File("data/properties.xml");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(propertiesXML);
				
				// Cast magic:
				doc.getDocumentElement().normalize();
				
				// Previous save information:
				NodeList nodes = doc.getElementsByTagName("previousSave");
				Element element = (Element) nodes.item(0);
				lastSaveLocation = ((Element)element.getElementsByTagName("location").item(0)).getAttribute("value");
				nameColour = ((Element)element.getElementsByTagName("nameColour").item(0)).getAttribute("value");
				name = ((Element)element.getElementsByTagName("name").item(0)).getAttribute("value");
				race = ((Element)element.getElementsByTagName("race").item(0)).getAttribute("value");
				quest = ((Element)element.getElementsByTagName("quest").item(0)).getAttribute("value");
				level = Integer.valueOf(((Element)element.getElementsByTagName("level").item(0)).getAttribute("value"));
				money = Integer.valueOf(((Element)element.getElementsByTagName("money").item(0)).getAttribute("value"));
				versionNumber = ((Element)element.getElementsByTagName("versionNumber").item(0)).getAttribute("value");
				
				// Settings:
				nodes = doc.getElementsByTagName("settings");
				element = (Element) nodes.item(0);
				fontSize = Integer.valueOf(((Element)element.getElementsByTagName("fontSize").item(0)).getAttribute("value"));
				lightTheme = ((((Element)element.getElementsByTagName("lightTheme").item(0)).getAttribute("value")).equals("true"));
				
				furryTailPenetrationContent = ((((Element)element.getElementsByTagName("furryTailPenetrationContent").item(0)).getAttribute("value")).equals("true"));
				nonConContent = ((((Element)element.getElementsByTagName("nonConContent").item(0)).getAttribute("value")).equals("true"));
				incestContent = ((((Element)element.getElementsByTagName("incestContent").item(0)).getAttribute("value")).equals("true"));
				forcedTransformationContent = ((((Element)element.getElementsByTagName("forcedTransformationContent").item(0)).getAttribute("value")).equals("true"));
				facialHairContent = ((((Element)element.getElementsByTagName("facialHairContent").item(0)).getAttribute("value")).equals("true"));
				pubicHairContent = ((((Element)element.getElementsByTagName("pubicHairContent").item(0)).getAttribute("value")).equals("true"));
				bodyHairContent = ((((Element)element.getElementsByTagName("bodyHairContent").item(0)).getAttribute("value")).equals("true"));
				
				newWeaponDiscovered = Boolean.valueOf(((Element)element.getElementsByTagName("newWeaponDiscovered").item(0)).getAttribute("value"));
				newClothingDiscovered = Boolean.valueOf(((Element)element.getElementsByTagName("newClothingDiscovered").item(0)).getAttribute("value"));
				newItemDiscovered = Boolean.valueOf(((Element)element.getElementsByTagName("newItemDiscovered").item(0)).getAttribute("value"));
				newRaceDiscovered = Boolean.valueOf(((Element)element.getElementsByTagName("newRaceDiscovered").item(0)).getAttribute("value"));
				
				overwriteWarning = ((((Element)element.getElementsByTagName("overwriteWarning").item(0)).getAttribute("value")).equals("true"));
				fadeInText = ((((Element)element.getElementsByTagName("fadeInText").item(0)).getAttribute("value")).equals("true"));
				
				if(element.getElementsByTagName("androgynousIdentification").item(0)!=null) {
					androgynousIdentification = AndrogynousIdentification.valueOf(((Element)element.getElementsByTagName("androgynousIdentification").item(0)).getAttribute("value"));
				}
				
				if(element.getElementsByTagName("humanEncountersLevel").item(0)!=null) {
					humanEncountersLevel = Integer.valueOf(((Element)element.getElementsByTagName("humanEncountersLevel").item(0)).getAttribute("value"));
				} else {
					humanEncountersLevel = 1;
				}
				
				if(element.getElementsByTagName("multiBreasts").item(0)!=null) {
					multiBreasts = Integer.valueOf(((Element)element.getElementsByTagName("multiBreasts").item(0)).getAttribute("value"));
				} else {
					multiBreasts = 1;
				}
				
				// Keys:
				nodes = doc.getElementsByTagName("keyBinds");
				element = (Element) nodes.item(0);
				for(int i=0; i<element.getElementsByTagName("binding").getLength(); i++){
					Element e = ((Element)element.getElementsByTagName("binding").item(i));
					
					if(!e.getAttribute("primaryBind").isEmpty())
						hotkeyMapPrimary.put(KeyboardAction.valueOf(e.getAttribute("bindName")), KeyCodeWithModifiers.fromString(e.getAttribute("primaryBind")));
					else
						hotkeyMapPrimary.put(KeyboardAction.valueOf(e.getAttribute("bindName")), null);
					
					if(!e.getAttribute("secondaryBind").isEmpty())
						hotkeyMapSecondary.put(KeyboardAction.valueOf(e.getAttribute("bindName")), KeyCodeWithModifiers.fromString(e.getAttribute("secondaryBind")));
					else
						hotkeyMapSecondary.put(KeyboardAction.valueOf(e.getAttribute("bindName")), null);
				}
				
				// Gender pronouns:
				nodes = doc.getElementsByTagName("genderPronouns");
				element = (Element) nodes.item(0);
				for(int i=0; i<element.getElementsByTagName("pronoun").getLength(); i++){
					Element e = ((Element)element.getElementsByTagName("pronoun").item(i));

					if(!e.getAttribute("feminineValue").isEmpty()) {
						genderPronounFemale.put(GenderPronoun.valueOf(e.getAttribute("pronounName")), e.getAttribute("feminineValue"));
					} else {
						genderPronounFemale.put(GenderPronoun.valueOf(e.getAttribute("pronounName")), GenderPronoun.valueOf(e.getAttribute("pronounName")).getFeminine());
					}

					if(!e.getAttribute("masculineValue").isEmpty()) {
						genderPronounMale.put(GenderPronoun.valueOf(e.getAttribute("pronounName")), e.getAttribute("masculineValue"));
					} else {
						genderPronounMale.put(GenderPronoun.valueOf(e.getAttribute("pronounName")), GenderPronoun.valueOf(e.getAttribute("pronounName")).getMasculine());
					}
				}
				
				// Gender preferences:
				nodes = doc.getElementsByTagName("genderPreferences");
				element = (Element) nodes.item(0);
				for(int i=0; i<element.getElementsByTagName("preference").getLength(); i++){
					Element e = ((Element)element.getElementsByTagName("preference").item(i));
					
					if(!e.getAttribute("gender").isEmpty())
						genderPreferencesMap.put(Gender.valueOf(e.getAttribute("gender")), Integer.valueOf(e.getAttribute("value")));
				}
				
				// Race preferences:
				nodes = doc.getElementsByTagName("furryPreferences");
				element = (Element) nodes.item(0);
				for(int i=0; i<element.getElementsByTagName("preferenceFeminine").getLength(); i++){
					Element e = ((Element)element.getElementsByTagName("preferenceFeminine").item(i));
					
					if(!e.getAttribute("race").isEmpty())
						raceFemininePreferencesMap.put(Race.valueOf(e.getAttribute("race")), FurryPreference.valueOf(e.getAttribute("preference")));
				}
				for(int i=0; i<element.getElementsByTagName("preferenceMasculine").getLength(); i++){
					Element e = ((Element)element.getElementsByTagName("preferenceMasculine").item(i));
					
					if(!e.getAttribute("race").isEmpty())
						raceMasculinePreferencesMap.put(Race.valueOf(e.getAttribute("race")), FurryPreference.valueOf(e.getAttribute("preference")));
				}
				
				// Discoveries:
				nodes = doc.getElementsByTagName("itemsDiscovered");
				element = (Element) nodes.item(0);
				for(int i=0; i<element.getElementsByTagName("itemType").getLength(); i++){
					Element e = ((Element)element.getElementsByTagName("itemType").item(i));
					
					if(!e.getAttribute("id").isEmpty()) {
						itemsDiscovered.add(ItemType.idToItemMap.get(e.getAttribute("id")));
					}
				}
				
				nodes = doc.getElementsByTagName("weaponsDiscovered");
				element = (Element) nodes.item(0);
				for(int i=0; i<element.getElementsByTagName("weaponType").getLength(); i++){
					Element e = ((Element)element.getElementsByTagName("weaponType").item(i));
					
					if(!e.getAttribute("id").isEmpty()) {
						weaponsDiscovered.add(WeaponType.idToWeaponMap.get(e.getAttribute("id")));
					}
				}
				
				nodes = doc.getElementsByTagName("clothingDiscovered");
				element = (Element) nodes.item(0);
				for(int i=0; i<element.getElementsByTagName("clothingType").getLength(); i++){
					Element e = ((Element)element.getElementsByTagName("clothingType").item(i));
					
					if(!e.getAttribute("id").isEmpty()) {
						clothingDiscovered.add(ClothingType.idToClothingMap.get(e.getAttribute("id")));
					}
				}
				
				nodes = doc.getElementsByTagName("racesDiscovered");
				element = (Element) nodes.item(0);
				for(int i=0; i<element.getElementsByTagName("raceDiscovery").getLength(); i++){
					Element e = ((Element)element.getElementsByTagName("raceDiscovery").item(i));
					
					if(!e.getAttribute("discovered").isEmpty()) {
						if(Boolean.valueOf(e.getAttribute("discovered"))) {
							this.racesDiscovered.add(Race.valueOf(e.getAttribute("race")));
						}
					}
					if(!e.getAttribute("advancedKnowledge").isEmpty()) {
						if(Boolean.valueOf(e.getAttribute("advancedKnowledge"))) {
							this.racesAdvancedKnowledge.add(Race.valueOf(e.getAttribute("race")));
						}
					}
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	// Add discoveries:
	
	public boolean addItemDiscovered(AbstractItemType itemType) {
		newItemDiscovered = itemsDiscovered.add(itemType);
		return newItemDiscovered;
	}
	
	public boolean isItemDiscovered(AbstractItemType itemType) {
		return itemsDiscovered.contains(itemType);
	}
	
	public boolean addClothingDiscovered(AbstractClothingType clothingType) {
		newClothingDiscovered = clothingDiscovered.add(clothingType);
		return newClothingDiscovered;
	}
	
	public boolean isClothingDiscovered(AbstractClothingType clothingType) {
		return clothingDiscovered.contains(clothingType);
	}
	
	public boolean addWeaponDiscovered(AbstractWeaponType weaponType) {
		newWeaponDiscovered = weaponsDiscovered.add(weaponType);
		return newWeaponDiscovered;
	}
	
	public boolean isWeaponDiscovered(AbstractWeaponType weaponType) {
		return weaponsDiscovered.contains(weaponType);
	}
	
	public boolean addRaceDiscovered(Race race) {
		newRaceDiscovered = racesDiscovered.add(race);
		if(newRaceDiscovered) {
			Main.game.addEvent(new EventLogEntryEncyclopediaUnlock(race.getName(), race.getColour()), true);
		}
		return newRaceDiscovered;
	}
	
	public boolean isRaceDiscovered(Race race) {
		return racesDiscovered.contains(race);
	}
	
	public boolean addAdvancedRaceKnowledge(Race race) {
		boolean added = racesAdvancedKnowledge.add(race);
		if(added) {
			Main.game.addEvent(new EventLogEntryEncyclopediaUnlock(race.getName()+" (Advanced)", race.getColour()), true);
		}
		return added;
	}
	
	public boolean isAdvancedRaceKnowledgeDiscovered(Race race) {
		return racesAdvancedKnowledge.contains(race);
	}
	
	// Getters/Setters for discoveries
	
	public boolean isNewWeaponDiscovered() {
		return newWeaponDiscovered;
	}

	public void setNewWeaponDiscovered(boolean newWeaponDiscovered) {
		this.newWeaponDiscovered = newWeaponDiscovered;
	}

	public boolean isNewClothingDiscovered() {
		return newClothingDiscovered;
	}

	public void setNewClothingDiscovered(boolean newClothingDiscovered) {
		this.newClothingDiscovered = newClothingDiscovered;
	}

	public boolean isNewItemDiscovered() {
		return newItemDiscovered;
	}

	public void setNewItemDiscovered(boolean newItemDiscovered) {
		this.newItemDiscovered = newItemDiscovered;
	}

	public boolean isNewRaceDiscovered() {
		return newRaceDiscovered;
	}

	public void setNewRaceDiscovered(boolean newRaceDiscovered) {
		this.newRaceDiscovered = newRaceDiscovered;
	}
}
