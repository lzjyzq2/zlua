function newCounter()
    local counter = 0;
    return function()
        counter = counter + 1
        return counter
    end
end

c1 = newCounter()
print(c1())
print(c1())
c2 = newCounter()
print(c2())
print(c1())
print(c2())