def calculate_essence(level, is_boss=False):
    """Calculate the total Essence for a given level."""
    if level < 0 or level > 1000:
        raise ValueError("Level must be between 0 and 1000")

    # Define the base Essence drop and multipliers for each tier
    base_essence = 10
    tier_multipliers = [
        1,    # Tier 0
        1.1,  # Tier 1
        1.2,  # Tier 2
        1.3,  # Tier 3
        1.4,  # Tier 4
        1.5,  # Tier 5
        1.6,  # Tier 6
        1.7,  # Tier 7
        1.8,  # Tier 8
        1.9,  # Tier 9
        10,   # Boss tier 0
        20,   # Boss tier 1
        30,   # Boss tier 2
        40,   # Boss tier 3
        50,   # Boss tier 4
        60,   # Boss tier 5
        70,   # Boss tier 6
        80,   # Boss tier 7
        90,   # Boss tier 8
        100   # Boss tier 9
    ]

    # Define the level ranges for each tier
    tier_ranges = [
        (0, 99),    # Tier 0
        (100, 199), # Tier 1
        (200, 299), # Tier 2
        (300, 399), # Tier 3
        (400, 499), # Tier 4
        (500, 599), # Tier 5
        (600, 699), # Tier 6
        (700, 799), # Tier 7
        (800, 899), # Tier 8
        (900, 999), # Tier 9
        (1000, 1000) # Boss tier
    ]

    # Adjust the tier index if it's a boss tier
    tier_index_offset = 10 if is_boss else 0

    # Calculate the accumulated Essence from previous tiers
    essence = 0
    for i, (start, end) in enumerate(tier_ranges):
        if level > end:
            # Add the full range of this tier
            essence += base_essence * (end - start + 1) * tier_multipliers[i + tier_index_offset]
        else:
            # Add the partial range of this tier
            essence += base_essence * (level - start + 1) * tier_multipliers[i + tier_index_offset]
            break

    # Subtract the base essence value
    essence -= base_essence

    return essence

def format_number(number):
    """Format the number with a separator for every three digits."""
    return f"{number:,}".replace(",", "'")

def main():
    print("Welcome to the Essence Calculator!")
    while True:
        try:
            # Ask the user for a level
            level_input = input("Enter the level of the being (or 0 to quit): ").strip()
            if level_input == "0":
                print("Exiting the calculator. Goodbye!")
                break

            is_boss = level_input.endswith("B")
            level = int(level_input[:-1]) if is_boss else int(level_input)

            if level < 0 or level > 1000:
                print("Please enter a level between 0 and 1000.")
            else:
                # Calculate the Essence and display the result
                essence = calculate_essence(level, is_boss)
                formatted_essence = format_number(essence)
                tier_type = "Boss" if is_boss else "Regular"
                print(f"The total Essence for {tier_type} level {level} is: {formatted_essence}")
        except ValueError:
            print("Invalid input. Please enter a numeric level.")

if __name__ == "__main__":
    main()