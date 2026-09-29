import hardcode
import random
import argparse
import datetime
import decimal
import pandas as pd

class CSVData:
    def __init__(self):
        self.df_inv_edible                          = None
        self.df_inv_nonedible                       = None
        self.df_employees                           = None
        self.df_menu_drinks                         = None
        self.df_menu_toppings                       = None
        self.df_join_menu_drinks_and_inv_edible     = None
        self.df_join_menu_toppings_and_inv_edible   = None

        self.list_of_dict_orders = None
        self.list_of_dict_orders_ids = None
        self.sales_total = decimal.Decimal('0.00')

        # Store the time the store opens and time the store closes on a day.
        # 11:00 am to 11:00 pm, CST.
        # 16:00 to 04:00, UTC (We'll just pretend like daylight savings doesn't exist).
        self.day_time_open = datetime.time(hour=16, minute=0, second=0, tzinfo=datetime.timezone.utc)
        # self.day_time_close = datetime.time(hour=4, minute=0, second=0, tzinfo=datetime.timezone.utc)
        # Store the number of seconds the store is open.
        self.day_timespan_open = 43200

        # 52 weeks of sales history (starting September 30, 2025, then ending September 30, 2026)
        self.start_date = datetime.date(year=2025, month=9, day=30)
        self.end_date = datetime.date(year=2026, month=9, day=30)
        self.day_delta = datetime.timedelta(days=1)

        # --We must have 3 peak days.--
        # High traffic of students on the first day of the Texas A&M Semester.
        self.peak1_date = datetime.date(year=2026, month=8, day=24)
        # High traffic of students on Texas A&M finals day no. 1.
        self.peak2_date = datetime.date(year=2026, month=5, day=4)
        # High traffic of students on Texas A&M finals day no. 2.
        self.peak3_date = datetime.date(year=2026, month=5, day=5)

    def store_hardcoded_data(self):
        self.df_inv_edible                         = pd.read_csv("inv_edible.csv")
        self.df_inv_nonedible                      = pd.read_csv("inv_nonedible.csv")
        self.df_employees                          = pd.read_csv("employees.csv")
        self.df_menu_drinks                        = pd.read_csv("menu_drinks.csv")
        self.df_menu_toppings                      = pd.read_csv("menu_toppings.csv")
        self.df_join_menu_drinks_and_inv_edible    = pd.read_csv("join_menu_drinks_and_inv_edible.csv")
        self.df_join_menu_toppings_and_inv_edible  = pd.read_csv("join_menu_toppings_and_inv_edible.csv")
        # print(self.df_inv_edible)
        # print(self.df_inv_nonedible)
        # print(self.df_employees)
        # print(self.df_menu_drinks)
        # print(self.df_menu_toppings)
        # print(self.df_join_menu_drinks_and_inv_edible)
        # print(self.df_join_menu_toppings_and_inv_edible)
        # self.df_menu_toppings.to_csv("output.csv", index=False)

    def gen_orders_csv(self):
        curr_date = self.start_date

        # Iterate from start date to end date.
        while curr_date <= self.end_date:
            # Generate order entries within a day.
            self.gen_entries_today(curr_date)

            # Advance to next date.
            curr_date += self.day_delta
        # 1 million in sales.

    def gen_entries_today(self, curr_date):
        # Adjust the number of orders made today to be of a "higher" range if the current date is a peak date.
        if curr_date == self.peak1_date or curr_date == self.peak2_date or curr_date == self.peak3_date:
            num_orders = random.randint(500, 700)
        else:
            num_orders = random.randint(200, 400)

        # Create random numbers of seconds in a list num_orders.
        # Have the random numbers of seconds be restricted to the number of seconds the store is open.
        random_seconds = [random.randint(0, self.day_timespan_open - 1) for _ in range(num_orders)]
        # Sort those chronologically (smallest to largest).
        random_seconds.sort()

        # Define the start time specifically for today.
        today_time_open = datetime.datetime.combine(curr_date, self.day_time_open)

        # Use the sorted random numbers of seconds to create a chronological list of time objects.
        # These are num_order times when an order was made in the day.
        random_today_times = [(today_time_open + datetime.timedelta(seconds=i)).time() for i in random_seconds]

        for time in random_today_times:
            self.gen_entry()

    def gen_entry(self):





def main():
    # Parse arguments
    parser = argparse.ArgumentParser(description="Generates CSV data files which can be loaded into database.")

    # Add argument --seed_orders_csv. If this script is executed with --seed_orders_csv appended, the argument
    # will be true.
    parser.add_argument('--seed_orders_csv', action="store_true", help="If this special flag is included, "
                                    "orders.csv will not be empty. Rather, it will be populated with a year's worth of "
                                    "fictional sales.")

    # Parse all arguments. Now the presence of argument --seed_orders_csv in executing the program is effectively
    # stored as either True or False in boolean seed_orders_csv located within args.
    args = parser.parse_args()

    # Generate hardcoded CSV files.
    hardcode.gen_csv_all()
    print("Generated all hardcoded CSV files.")

    # Only fill orders.csv with fictional data if --seed_orders_csv flag is present.
    if args.seed_orders_csv:
        # Class CSVData manages parsing hardcoded CSV files to create orders.csv file with seeded data.
        dynamic = CSVData()

        # Popular CSVData DataFrames with hardcoded CSVs appropriately.
        dynamic.store_hardcoded_data()

        # Generate the orders.csv with seeded entires.
        dynamic.gen_orders_csv()


if __name__ == "__main__":
    main()