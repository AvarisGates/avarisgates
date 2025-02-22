def calculate_level(exp, rarity):
    base_xp = 100  # Base XP requirement for level 1
    rarity_multipliers = {
        "COMMON": 1.0,
        "UNCOMMON": 1.5,
        "RARE": 3.0,
        "EPIC": 6.0,
        "LEGENDARY": 12.0
    }
    
    if rarity not in rarity_multipliers:
        raise ValueError("Invalid rarity. Choose from COMMON, UNCOMMON, RARE, EPIC, LEGENDARY.")
    
    multiplier = rarity_multipliers[rarity]
    level = 0
    total_xp = 0
    
    while total_xp <= exp and level < 101s:
        level += 1
        total_xp += base_xp * (level ** 1.005) * multiplier
        total_xp = (total_xp // 100) * 100  # Round down to the nearest multiple of 100
    
    return level - 1

def calculate_xp_for_level(level, rarity):
    base_xp = 100  # Base XP requirement for level 1
    rarity_multipliers = {
        "COMMON": 1.0,
        "UNCOMMON": 1.5,
        "RARE": 3.0,
        "EPIC": 6.0,
        "LEGENDARY": 12.0
    }
    
    if rarity not in rarity_multipliers:
        raise ValueError("Invalid rarity. Choose from COMMON, UNCOMMON, RARE, EPIC, LEGENDARY.")
    
    multiplier = rarity_multipliers[rarity]
    total_xp = 0
    
    for lvl in range(1, level + 1):
        total_xp += base_xp * (lvl ** 1.005) * multiplier
        total_xp = (total_xp // 100) * 100  # Round down to the nearest multiple of 100
    
    return total_xp

def main():
    print("Welcome to the Level Calculator!")
    while True:
        try:
            exp = int(input("Enter the experience amount (or 0 to quit): "))
            if exp == 0:
                print("Exiting the calculator. Goodbye!")
                break
            
            rarity = input("Enter the class rarity (COMMON, UNCOMMON, RARE, EPIC, LEGENDARY): ").upper()
            level = calculate_level(exp, rarity)
            print(f"The calculated level for {exp} XP with {rarity} rarity is: {level}")
            
            target_level = int(input("Enter the target level to calculate required XP: "))
            xp_required = calculate_xp_for_level(target_level, rarity)
            print(f"The experience required to reach level {target_level} with {rarity} rarity is: {xp_required}")
        except ValueError as e:
            print(f"Invalid input: {e}")

if __name__ == "__main__":
    main()