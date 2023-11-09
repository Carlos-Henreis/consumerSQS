import sys
from awsglue.transforms import *
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from awsglue.context import GlueContext
from awsglue.job import Job

args = getResolvedOptions(sys.argv, ['JOB_NAME'])

sc = SparkContext()
glueContext = GlueContext(sc)
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args['JOB_NAME'], args)

# Define o caminho de entrada e saída
input_bucket = "s3://teste-bucket/cliente/"
output_bucket = "s3://destino-bucket/"

# Cria o DynamicFrame
dynamic_frame = glueContext.create_dynamic_frame.from_catalog(
    database = "seu_database_name",
    table_name = "cliente_target",
    transformation_ctx = "dynamic_frame"
)

# Converte para Parquet
parquet_dynamic_frame = ApplyMapping.apply(
    frame = dynamic_frame,
    mappings = [
        ("coluna_csv", "tipo_parquet", "nome_coluna_parquet")
        # Adicione mais mapeamentos conforme necessário
    ],
    transformation_ctx = "parquet_dynamic_frame"
)

# Escreve no bucket de saída
glueContext.write_dynamic_frame.from_options(
    frame = parquet_dynamic_frame,
    connection_type = "s3",
    connection_options = {"path": output_bucket},
    format = "parquet",
    transformation_ctx = "write_dynamic_frame"
)

job.commit()
