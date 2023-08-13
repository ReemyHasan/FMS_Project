import rethinkdb as r
import datetime
import pytz
import os

dir_path = os.path.dirname(os.path.realpath(__file__))

rdb = r.RethinkDB()
conn = rdb.connect("172.29.3.220",28015).repl()
s = rdb.db("Traps").table("Raw-Traps").run()

time_threshold = 60*1000
tz = pytz.timezone('Etc/GMT-3')
current_datetime = datetime.datetime.now(tz)
timestamp = current_datetime.timestamp()*1000



e = rdb.db("Traps").table('Raw-Traps').filter((timestamp - rdb.row['date']) > time_threshold).delete().run()
#print(e)
with open(os.path.join(dir_path, 'output.txt'), 'a') as f:
    f.write(str(current_datetime))
    f.write(": ")
    f.write(str(e))
    f.write('\n')
conn.close()