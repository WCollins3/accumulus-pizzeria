create view vote as
select topping_name, count(*) as votes
from topping
group by topping_name
order by votes desc;