Takes input file name as argumenet.
Input file contains space seperated values (just like command line arguments)

See input.txt for example.

input syntax :
filename_to_be_splitted -f ruleset_file1.txt ruleset_file2.txt ... -c COLUMN_NO_1_to_match COLUMN_NO_2_to_match -d delimiter

i.e `student_list.csv -f set1.txt -c 10 -d ,`

Put all files to be splitted in the root directory. Generated files also would be in the root directory of project.
For multiple files specify the list in input.txt file, each on new line.
