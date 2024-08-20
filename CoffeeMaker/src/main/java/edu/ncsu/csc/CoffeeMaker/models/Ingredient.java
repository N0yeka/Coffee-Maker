package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Class to keep track of any arbitrary Ingredient object
 */
@Entity
public class Ingredient extends DomainObject {

	/** Recipe id */
	@Id
	@GeneratedValue
	private Long id;

	/** Ingredient name */
	@NotNull
	private String name;

	/** Ingredient amount */
	@Min(0)
	private Integer amount;

	/**
	 * Ingredient constructor
	 */
	public Ingredient() {
	}

	/**
	 * Ingredient constructor that takes an Ingredient name
	 * and amount
	 * @param name The name of the Ingredient to initialize
	 * @param amount The amount of the Ingredient to initialize
	 */
	public Ingredient(String name, @Min(0) Integer amount) {
		this.name = name;
		this.amount = amount;
	}

	/**
	 * Gets the name of an Ingredient
	 * @return The name of an Ingredient
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of an Ingredient to the specified name
	 * @param name The name to change the Ingredient name to
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the amount of an Ingredient
	 * @return The amount of an Ingredient
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * Sets the amount of an Ingredient to the specified amount
	 * @param amount The amount to set the Ingredient amount to
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * Sets the ID of an Ingredient to the specified Id
	 * @param id the ID to change the Ingredient ID to
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public Serializable getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", ingredient=" + name + ", amount=" + amount + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingredient other = (Ingredient) obj;
		return Objects.equals(name, other.name);
	}

}
