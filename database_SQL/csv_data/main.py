import csv
import random
import datetime

def gen_csv_orders():
    with open("orders.csv", 'w', newline='') as csvfile:
        header = ["id_order", "completed", "time_created_at", "time_completed_at", "total_spent", "id_employee", "tip",
                  "id_drink", "id_topping1", "id_topping2", "ice_level", "sugar_level", "hot_chosen"]
        writer = csv.DictWriter(csvfile, fieldnames=header)
        writer.writeheader()

    # 52 weeks of sales history (starting September 30, 2025, then ending September 30, 2026)
    start_date = datetime.date(2025, 9, 30)
    end_date = datetime.date(2026, 9, 30)

    # We must have 3 peak days.
    # 1 million in sales.

def gen_csv_hardcode(csv_name):
    with open(csv_name + ".csv", 'w', newline='') as csvfile:
        if csv_name == "inv_edible":
            header = ["id_edible", "name", "num_servings"]
            writer = csv.DictWriter(csvfile, fieldnames=header)
            writer.writeheader()
            writer.writerow({"id_edible": 1, "name": "Black Tea", "num_servings": 2000})
            writer.writerow({"id_edible": 2, "name": "Green Tea", "num_servings": 1600})
            writer.writerow({"id_edible": 3, "name": "Creamer", "num_servings": 650})
            writer.writerow({"id_edible": 4, "name": "Sugar Syrup", "num_servings": 1300})
            writer.writerow({"id_edible": 5, "name": "Tapioca Pearls", "num_servings": 600})
            writer.writerow({"id_edible": 6, "name": "Honey", "num_servings": 500})
            writer.writerow({"id_edible": 7, "name": "Ice", "num_servings": 1300})
            writer.writerow({"id_edible": 8, "name": "Coffee", "num_servings": 550})
            writer.writerow({"id_edible": 9, "name": "Creama Topping", "num_servings": 250})
            writer.writerow({"id_edible": 10, "name": "Coffee Jelly", "num_servings": 300})
            writer.writerow({"id_edible": 11, "name": "Hokkaido Powder", "num_servings": 170})
            writer.writerow({"id_edible": 12, "name": "Thai Tea Powder", "num_servings": 500})
            writer.writerow({"id_edible": 13, "name": "Taro Powder", "num_servings": 330})
            writer.writerow({"id_edible": 14, "name": "Lychee Jelly", "num_servings": 200})
            writer.writerow({"id_edible": 15, "name": "Coconut Syrup", "num_servings": 170})
            writer.writerow({"id_edible": 16, "name": "Mango Jam", "num_servings": 150})
            writer.writerow({"id_edible": 17, "name": "Passion Fruit Jam", "num_servings": 150})
            writer.writerow({"id_edible": 18, "name": "Berry Juice", "num_servings": 170})
            writer.writerow({"id_edible": 19, "name": "Lychee Juice", "num_servings": 170})
            writer.writerow({"id_edible": 20, "name": "Peach Jam", "num_servings": 150})
            writer.writerow({"id_edible": 21, "name": "Honey Jelly", "num_servings": 160})
            writer.writerow({"id_edible": 22, "name": "Lemon Juice", "num_servings": 400})
            writer.writerow({"id_edible": 23, "name": "Milk", "num_servings": 530})
            writer.writerow({"id_edible": 24, "name": "Brown Sugar Syrup", "num_servings": 670})
            writer.writerow({"id_edible": 25, "name": "Coconut Milk", "num_servings": 200})
            writer.writerow({"id_edible": 26, "name": "Strawberry Jam", "num_servings": 150})
            writer.writerow({"id_edible": 27, "name": "Pudding", "num_servings": 200})
            writer.writerow({"id_edible": 28, "name": "Crystal Boba", "num_servings": 200})
            writer.writerow({"id_edible": 29, "name": "Mango Popping Boba", "num_servings": 200})
            writer.writerow({"id_edible": 30, "name": "Strawberry Popping Boba", "num_servings": 140})
            writer.writerow({"id_edible": 31, "name": "Ice Cream", "num_servings": 170})
            writer.writerow({"id_edible": 32, "name": "Ice", "num_servings": 2000})

        if csv_name == "inv_nonedible":
            header = ["id_nonedible", "name", "amount"]
            writer = csv.DictWriter(csvfile, fieldnames=header)
            writer.writeheader()
            writer.writerow({"id_nonedible": 1, "name": "Lids", "amount": 400})
            writer.writerow({"id_nonedible": 2, "name": "Cups", "amount": 500})
            writer.writerow({"id_nonedible": 3, "name": "Straws", "amount": 300})
            writer.writerow({"id_nonedible": 4, "name": "Lid Covers", "amount": 500})
            writer.writerow({"id_nonedible": 5, "name": "Napkins", "amount": 1000})
            writer.writerow({"id_nonedible": 6, "name": "Cup Holders", "amount": 350})
            writer.writerow({"id_nonedible": 7, "name": "Physical Gift Cards", "amount": 30})
            writer.writerow({"id_nonedible": 8, "name": "Spoons", "amount": 10})
            writer.writerow({"id_nonedible": 9, "name": "Bags", "amount": 350})

        if csv_name == "employees":
            header = ["id_employee", "name", "position", "phone_number", "current_pay_rate", "hours_worked_for_week"]
            writer = csv.DictWriter(csvfile, fieldnames=header)
            writer.writeheader()
            writer.writerow(
                {"id_employee": 1, "name": "Carter Guzman", "position": "Barista", "phone_number": 3052297164,
                 "current_pay_rate": 11.6, "hours_worked_for_week": 12})
            writer.writerow(
                {"id_employee": 2, "name": "Robin Acosta", "position": "Barista", "phone_number": 9206762586,
                 "current_pay_rate": 11.6, "hours_worked_for_week": 15})
            writer.writerow(
                {"id_employee": 3, "name": "Mariana Davies", "position": "Barista", "phone_number": 7694254933,
                 "current_pay_rate": 11.6, "hours_worked_for_week": 9})
            writer.writerow(
                {"id_employee": 4, "name": "Trent Malone", "position": "Barista", "phone_number": 4724646482,
                 "current_pay_rate": 11.6, "hours_worked_for_week": 12})
            writer.writerow(
                {"id_employee": 5, "name": "Shayne Topp", "position": "Manager", "phone_number": 4724446764,
                 "current_pay_rate": 20.0, "hours_worked_for_week": 20})
            writer.writerow(
                {"id_employee": 6, "name": "Kristie Cole", "position": "Barista", "phone_number": 3053864982,
                 "current_pay_rate": 10.5, "hours_worked_for_week": 15})
            writer.writerow(
                {"id_employee": 7, "name": "Percy Jackson", "position": "Barista", "phone_number": 7625999555,
                 "current_pay_rate": 10.5, "hours_worked_for_week": 12})
            writer.writerow(
                {"id_employee": 8, "name": "Johanna Mason", "position": "Manager", "phone_number": 3052514454,
                 "current_pay_rate": 18.5, "hours_worked_for_week": 20})
            writer.writerow(
                {"id_employee": 9, "name": "Amy Santiago", "position": "Barista", "phone_number": 5057617199,
                 "current_pay_rate": 10.5, "hours_worked_for_week": 18})
            writer.writerow(
                {"id_employee": 10, "name": "Michael Scott", "position": "Manager", "phone_number": 5056467984,
                 "current_pay_rate": 22.5, "hours_worked_for_week": 20})
            writer.writerow(
                {"id_employee": 11, "name": "Eleanor Shelstrop", "position": "Barista", "phone_number": 9838455494,
                 "current_pay_rate": 10.0, "hours_worked_for_week": 0})
            writer.writerow(
                {"id_employee": 12, "name": "Sarika Ram", "position": "Barista", "phone_number": 2485889939,
                 "current_pay_rate": 10.0, "hours_worked_for_week": 6})
            writer.writerow(
                {"id_employee": 13, "name": "Asher Blevins", "position": "Barista", "phone_number": 4722910725,
                 "current_pay_rate": 10.0, "hours_worked_for_week": 12})
            writer.writerow(
                {"id_employee": 14, "name": "Paul Taele", "position": "Barista", "phone_number": 5052358937,
                 "current_pay_rate": 10.0, "hours_worked_for_week": 15})

        if csv_name == "menu_drink":
            header = ["id_drink", "name", "price", "hot_available", "is_non_caffeinated"]
            writer = csv.DictWriter(csvfile, fieldnames=header)
            writer.writeheader()
            writer.writerow({"id_drink": 1, "name": "Classic Pearl Milk Tea", "price": 5.8, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 2, "name": "Honey Pearl Milk Tea", "price": 6.0, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 3, "name": "Coffee Crema", "price": 6.5, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 4, "name": "Coffee Milk Tea w/ Coffee Jelly", "price": 6.25, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 5, "name": "Hokkaido Pearl Milk Tea", "price": 6.25, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 6, "name": "Thai Pearl Milk Tea", "price": 6.25, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 7, "name": "Taro Pearl Milk Tea", "price": 6.25, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 8, "name": "Mango Green Milk Tea", "price": 6.5, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 9, "name": "Golden Retriever", "price": 6.75, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 10, "name": "Coconut Pearl Milk Tea", "price": 6.75, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 11, "name": "Classic Tea", "price": 4.65, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 12, "name": "Honey Tea", "price": 4.85, "hot_available": True,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 13, "name": "Mango Green Tea", "price": 5.8, "hot_available": False,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 14, "name": "Passion Chess", "price": 6.25, "hot_available": False,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 15, "name": "Berry Lychee Burst", "price": 6.25, "hot_available": False,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 16, "name": "Peach Tea w/ Honey Jelly", "price": 6.25, "hot_available": False,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 17, "name": "Mango & Passion Fruit Tea", "price": 6.25, "hot_available": False,
                            "is_non_caffeinated": False})
            writer.writerow({"id_drink": 18, "name": "Honey Lemonade", "price": 5.2, "hot_available": False,
                            "is_non_caffeinated": True})
            writer.writerow({"id_drink": 19, "name": "Tiger Boba", "price": 6.5, "hot_available": False,
                            "is_non_caffeinated": True})
            writer.writerow({"id_drink": 20, "name": "Strawberry Coconut", "price": 6.5, "hot_available": True,
                            "is_non_caffeinated": True})

        if csv_name == "menu_toppings":
            header = ["id_topping", "name", "price"]
            writer = csv.DictWriter(csvfile, fieldnames=header)
            writer.writeheader()
            writer.writerow({"id_topping": 1, "name": "Pearls (Boba)", "price": 0.75})
            writer.writerow({"id_topping": 2, "name": "Coffee Jelly", "price": 0.75})
            writer.writerow({"id_topping": 3, "name": "Pudding", "price": 0.75})
            writer.writerow({"id_topping": 4, "name": "Lychee Jelly", "price": 0.75})
            writer.writerow({"id_topping": 5, "name": "Honey Jelly", "price": 0.75})
            writer.writerow({"id_topping": 6, "name": "Crystal Boba", "price": 1.0})
            writer.writerow({"id_topping": 7, "name": "Mango Popping Boba", "price": 1.0})
            writer.writerow({"id_topping": 8, "name": "Strawberry Popping Boba", "price": 1.0})
            writer.writerow({"id_topping": 9, "name": "Ice Cream", "price": 1.0})
            writer.writerow({"id_topping": 10, "name": "Creama", "price": 1.0})

        if csv_name == "join_menu_drink_and_inv_edible":
            header = ["id_join_menu_drink_and_inv_edible", "id_drink", "id_edible"]
            writer = csv.DictWriter(csvfile, fieldnames=header)
            writer.writeheader()
            writer.writerow({"id_join_menu_drink_and_inv_edible": 1, "id_drink": 1, "id_edible": 1})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 2, "id_drink": 1, "id_edible": 3})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 3, "id_drink": 1, "id_edible": 5})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 4, "id_drink": 1, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 5, "id_drink": 1, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 6, "id_drink": 2, "id_edible": 1})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 7, "id_drink": 2, "id_edible": 3})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 8, "id_drink": 2, "id_edible": 6})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 9, "id_drink": 2, "id_edible": 5})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 10, "id_drink": 2, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 11, "id_drink": 3, "id_edible": 8})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 12, "id_drink": 3, "id_edible": 9})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 13, "id_drink": 3, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 14, "id_drink": 3, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 15, "id_drink": 4, "id_edible": 1})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 16, "id_drink": 4, "id_edible": 8})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 17, "id_drink": 4, "id_edible": 3})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 18, "id_drink": 4, "id_edible": 10})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 19, "id_drink": 4, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 20, "id_drink": 4, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 21, "id_drink": 5, "id_edible": 1})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 22, "id_drink": 5, "id_edible": 11})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 23, "id_drink": 5, "id_edible": 3})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 24, "id_drink": 5, "id_edible": 5})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 25, "id_drink": 5, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 26, "id_drink": 5, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 27, "id_drink": 6, "id_edible": 12})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 28, "id_drink": 6, "id_edible": 3})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 29, "id_drink": 6, "id_edible": 5})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 30, "id_drink": 6, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 31, "id_drink": 6, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 32, "id_drink": 7, "id_edible": 1})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 33, "id_drink": 7, "id_edible": 13})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 34, "id_drink": 7, "id_edible": 3})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 35, "id_drink": 7, "id_edible": 5})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 36, "id_drink": 7, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 37, "id_drink": 7, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 38, "id_drink": 8, "id_edible": 2})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 39, "id_drink": 8, "id_edible": 3})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 40, "id_drink": 8, "id_edible": 16})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 41, "id_drink": 8, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 42, "id_drink": 8, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 43, "id_drink": 9, "id_edible": 1})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 44, "id_drink": 9, "id_edible": 3})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 45, "id_drink": 9, "id_edible": 5})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 46, "id_drink": 9, "id_edible": 27})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 47, "id_drink": 9, "id_edible": 10})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 48, "id_drink": 9, "id_edible": 14})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 49, "id_drink": 9, "id_edible": 21})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 50, "id_drink": 9, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 51, "id_drink": 9, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 52, "id_drink": 10, "id_edible": 1})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 53, "id_drink": 10, "id_edible": 15})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 54, "id_drink": 10, "id_edible": 3})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 55, "id_drink": 10, "id_edible": 5})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 56, "id_drink": 10, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 57, "id_drink": 10, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 58, "id_drink": 11, "id_edible": 1})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 59, "id_drink": 11, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 60, "id_drink": 11, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 61, "id_drink": 12, "id_edible": 1})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 62, "id_drink": 12, "id_edible": 6})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 63, "id_drink": 12, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 64, "id_drink": 13, "id_edible": 2})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 65, "id_drink": 13, "id_edible": 16})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 66, "id_drink": 13, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 67, "id_drink": 13, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 68, "id_drink": 14, "id_edible": 2})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 69, "id_drink": 14, "id_edible": 17})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 70, "id_drink": 14, "id_edible": 5})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 71, "id_drink": 14, "id_edible": 14})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 72, "id_drink": 14, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 73, "id_drink": 14, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 74, "id_drink": 15, "id_edible": 2})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 75, "id_drink": 15, "id_edible": 18})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 76, "id_drink": 15, "id_edible": 19})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 77, "id_drink": 15, "id_edible": 30})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 78, "id_drink": 15, "id_edible": 14})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 79, "id_drink": 15, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 80, "id_drink": 15, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 81, "id_drink": 16, "id_edible": 1})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 82, "id_drink": 16, "id_edible": 20})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 83, "id_drink": 16, "id_edible": 21})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 84, "id_drink": 16, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 85, "id_drink": 16, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 86, "id_drink": 17, "id_edible": 2})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 87, "id_drink": 17, "id_edible": 16})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 88, "id_drink": 17, "id_edible": 17})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 89, "id_drink": 17, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 90, "id_drink": 17, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 91, "id_drink": 18, "id_edible": 22})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 92, "id_drink": 18, "id_edible": 6})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 93, "id_drink": 18, "id_edible": 7})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 94, "id_drink": 19, "id_edible": 23})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 95, "id_drink": 19, "id_edible": 24})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 96, "id_drink": 19, "id_edible": 5})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 97, "id_drink": 20, "id_edible": 26})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 98, "id_drink": 20, "id_edible": 25})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 99, "id_drink": 20, "id_edible": 4})
            writer.writerow({"id_join_menu_drink_and_inv_edible": 100, "id_drink": 20, "id_edible": 7})

        if csv_name == "join_menu_topping_and_inv_edible":
            header = ["id_join_menu_topping_and_inv_edible", "id_topping", "id_edible"]
            writer = csv.DictWriter(csvfile, fieldnames=header)
            writer.writeheader()
            writer.writerow({"id_join_menu_topping_and_inv_edible": 1, "id_topping": 1, "id_edible": 5})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 2, "id_topping": 2, "id_edible": 10})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 3, "id_topping": 3, "id_edible": 27})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 4, "id_topping": 4, "id_edible": 14})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 5, "id_topping": 5, "id_edible": 21})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 6, "id_topping": 6, "id_edible": 28})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 7, "id_topping": 7, "id_edible": 29})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 8, "id_topping": 8, "id_edible": 30})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 9, "id_topping": 9, "id_edible": 31})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 10, "id_topping": 10, "id_edible": 9})

        if csv_name == "join_menu_topping_and_inv_edible":
            header = ["id_join_menu_topping_and_inv_edible", "id_topping", "id_edible"]
            writer = csv.DictWriter(csvfile, fieldnames=header)
            writer.writeheader()
            writer.writerow({"id_join_menu_topping_and_inv_edible": 1, "id_topping": 1, "id_edible": 5})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 2, "id_topping": 2, "id_edible": 10})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 3, "id_topping": 3, "id_edible": 27})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 4, "id_topping": 4, "id_edible": 14})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 5, "id_topping": 5, "id_edible": 21})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 6, "id_topping": 6, "id_edible": 28})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 7, "id_topping": 7, "id_edible": 29})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 8, "id_topping": 8, "id_edible": 30})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 9, "id_topping": 9, "id_edible": 31})
            writer.writerow({"id_join_menu_topping_and_inv_edible": 10, "id_topping": 10, "id_edible": 9})


def main():
    gen_csv_hardcode("inv_edible")
    gen_csv_hardcode("inv_nonedible")
    gen_csv_hardcode("employees")
    gen_csv_hardcode("menu_drink")
    gen_csv_hardcode("menu_toppings")
    gen_csv_hardcode("join_menu_drink_and_inv_edible")
    gen_csv_hardcode("join_menu_topping_and_inv_edible")
    gen_csv_orders()

if __name__ == "__main__":
    main()